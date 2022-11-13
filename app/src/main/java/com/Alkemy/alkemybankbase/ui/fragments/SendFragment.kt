package com.Alkemy.alkemybankbase.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.databinding.FragmentSendBinding
import com.Alkemy.alkemybankbase.presentation.SendViewModel
import com.Alkemy.alkemybankbase.utils.LogBundle
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendFragment : Fragment() {
    private var _binding : FragmentSendBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SendViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentSendBinding.inflate(inflater,container,false)
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        setupListener()
        return binding.root
    }
    private fun setupListener() {
        with(binding) {
            btnSend.setOnClickListener {
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Send Pressed","enviar_pressed")
                lifecycleScope.launch {
                    viewModel.send("auth",etDestination.text.toString().toInt(),etConcept.text.toString(),etAmount.text.toString().toInt())
                    if (viewModel.sendResponse.status==200) {
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Send Succeeded","enviar_dinero_success")
                        showDialog("Aceptar", "Envio de dinero exitoso")
                    } else if (viewModel.sendError.isNotBlank()) {
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Send Failed","enviar_dinero_error")
                        showAlert("Error",viewModel.sendError)
                    }
                }
            }
        }
    }
    private fun navigateSend(){
        val intent = Intent(requireContext(),SendFragment::class.java)
        startActivity(intent)
    }
    private fun showDialog(title: String,message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){
                dialog,id -> navigateSend()
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
}