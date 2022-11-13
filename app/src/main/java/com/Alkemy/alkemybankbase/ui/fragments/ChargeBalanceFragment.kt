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
import com.Alkemy.alkemybankbase.databinding.FragmentChargeBalanceBinding
import com.Alkemy.alkemybankbase.presentation.ChargeViewModel
import com.Alkemy.alkemybankbase.ui.activities.HomeActivity
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChargeBalanceFragment : Fragment() {
    private var _binding : FragmentChargeBalanceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChargeViewModel by viewModels()
    private lateinit var auth:String
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentChargeBalanceBinding.inflate(inflater,container,false)

        setupObservers()
        setupListeners()

        auth = "${SessionManager.getToken(requireContext())}"

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        return binding.root

    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(viewLifecycleOwner) {
            // enable or disable button
            binding.btnChargeBalance.isEnabled = it
        }
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            // show loading and hide ui
            binding.prgbar.isVisible = it
        }
        viewModel.amountErrorResourceLiveData.observe(viewLifecycleOwner){ resId ->
            //show amount error
            binding.etAmount.error = getString(resId)
        }
        viewModel.conceptErrorResourceLiveData.observe(viewLifecycleOwner){ resId ->
            //show concept error
            binding.etConcept.error = getString(resId)
        }
    }
    private fun setupListeners() {
        with(binding) {
            btnChargeBalance.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.topUp(requireContext(), auth, etAmount.text.toString().toInt(), etConcept.text.toString())

                    if (viewModel.topUpResponse.status==200) {
                        showDialog("Aceptar", "Carga de saldo exitosa")
                    } else if (!viewModel.errorLiveData.value.isNullOrBlank()) {
                        showAlert("Aceptar", viewModel.errorLiveData.value.toString())
                    }
                }
            }
            etAmount.afterTextChanged {
                viewModel.validateForm(
                    etAmount.text.toString().toIntOrNull() ?: 0,
                    etConcept.text.toString(),
                )
            }
            etConcept.afterTextChanged {
                viewModel.validateForm(
                    etAmount.text.toString().toIntOrNull() ?: 0,
                    etConcept.text.toString(),
                )
            }
            btnChargeBalance.isEnabled = false
        }
    }
    private fun showDialog(title: String,message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){
                dialog,id -> navigateToHome()
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(title:String,message:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToHome(){
        val intent = Intent(requireContext(),HomeActivity::class.java)
        startActivity(intent)
    }
}