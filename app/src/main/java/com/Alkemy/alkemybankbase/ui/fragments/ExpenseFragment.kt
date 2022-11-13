package com.Alkemy.alkemybankbase.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.databinding.FragmentExpenseBinding
import com.Alkemy.alkemybankbase.presentation.ExpensesViewModel
import com.Alkemy.alkemybankbase.ui.activities.HomeActivity
import com.Alkemy.alkemybankbase.utils.LogBundle
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExpenseFragment : Fragment() {
    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpensesViewModel by viewModels()
    private lateinit var auth: String
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)

        setupObservers()
        setupListeners()

        auth = "${SessionManager.getToken(requireContext())}"
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        return binding.root
    }

    private fun setupListeners() {
        with(binding) {
            btnAddExpense.setOnClickListener {
                LogBundle.logBundleAnalytics(
                    firebaseAnalytics,
                    "Carga Datos Pressed",
                    "carga_datos_pressed"
                )
                //Yes, I know it doesn't make sense for the log to be named "Load Data Pressed"
                //when it is just a transaction. But that is what the ticket asked for ¯\_(ツ)_/¯
                lifecycleScope.launch {
                    viewModel.addExpense(
                        requireContext(),
                        auth,
                        etAmount.text.toString().toInt(),
                        etConcept.text.toString(),
                        etDestination.text.toString().toInt()
                    )
                    if (viewModel.expenseResponse.accountId != 0) {
                        LogBundle.logBundleAnalytics(
                            firebaseAnalytics,
                            "Carga Datos Success",
                            "carga_datos_success"
                        )
                        showDialog("Gasto Registrado", "Success")
                    }
                    if (!viewModel.errorLiveData.value.isNullOrBlank()) {
                        LogBundle.logBundleAnalytics(
                            firebaseAnalytics,
                            "Carga Datos Error",
                            "carga_datos_error"
                        )
                        showDialog(viewModel.errorLiveData.value.toString(), "Error")
                    }
                }
            }
            etAmount.afterTextChanged {
                viewModel.validateForm(
                    etConcept.text.toString(),
                    etAmount.text.toString().toIntOrNull() ?: 0,
                    etDestination.text.toString().toIntOrNull() ?: 0,
                )
            }
            etConcept.afterTextChanged {
                viewModel.validateForm(
                    etConcept.text.toString(),
                    etAmount.text.toString().toIntOrNull() ?: 0,
                    etDestination.text.toString().toIntOrNull() ?: 0,
                )
            }
            etDestination.afterTextChanged {
                viewModel.validateForm(
                    etConcept.text.toString(),
                    etAmount.text.toString().toIntOrNull() ?: 0,
                    etDestination.text.toString().toIntOrNull() ?: 0,
                )
            }
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            etDate.setText(formatter.format(time))
            btnAddExpense.isEnabled = false
            etDate.isEnabled = false
        }
    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(viewLifecycleOwner) {
            binding.btnAddExpense.isEnabled = it
        }

        viewModel.conceptErrorResourceLiveData.observe(viewLifecycleOwner) { resId ->
            binding.etConcept.error = getString(resId)
        }
        viewModel.amountErrorResourceLiveData.observe(viewLifecycleOwner) { resId ->
            binding.etAmount.error = getString(resId)
        }
        viewModel.toAccountIdErrorResourceLiveData.observe(viewLifecycleOwner) { resId ->
            binding.etDestination.error = getString(resId)
        }
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            // show loading and hide ui
            binding.prgbar.isVisible = it
        }
    }

    private fun showDialog(message: String, status: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(status)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener() { dialog, id ->
            if (status == "Success"){
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}