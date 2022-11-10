package com.Alkemy.alkemybankbase.ui.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.databinding.ActivitySignUpBinding
import com.Alkemy.alkemybankbase.presentation.SignUpViewModel
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setuplistener()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(this) {
            // enable or disable button
            binding.btnSignUp.isEnabled = it
        }
        viewModel.confirmPasswordErrorResourceIdLiveData.observe(this) { resId ->
            //show confirm password error
            binding.etConfirmPassword.error = getString(resId)
        }
        viewModel.emailErrorResourceIdLiveData.observe(this) { resId ->
            //show email error
            binding.etEmail.error = getString(resId)
        }
        viewModel.passwordErrorResourceIdLiveData.observe(this) { resId ->
            //show password error
            binding.etPassword.error = getString(resId)
        }
        viewModel.isLoading.observe(this) {
            if(it) showLoading()
            else stopLoading()
        }
    }

    private fun setuplistener() {
        with(binding) {
            btnSignUp.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("message", "Sign Up Pressed")
                firebaseAnalytics.logEvent("register_pressed", bundle)
                lifecycleScope.launch {
                    viewModel.createUser(etFirstname.text.toString(),etLastname.text.toString(),etEmail.text.toString(),etPassword.text.toString())
                    if (viewModel.userResponse.email.isNotBlank()) {
                        showCorrectDialog("Great!","User was successfully registered")
                        bundle.putString("message", "Sign Up Succeeded")
                        firebaseAnalytics.logEvent("sign_up_success", bundle)
                    } else if(viewModel.userError.isNotBlank()){
                        showAlert("Error",viewModel.userError)
                        bundle.putString("message", "Sign Up Failed")
                        firebaseAnalytics.logEvent("sign_up_error", bundle)

                    }
                }
                    //Todo implement function and make call to viewmodel
                /*    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()

                    ).addOnCompleteListener {
                        if (!it.isSuccessful) {
                            showAlert("Error", "No se pudo registrar el usuario en firebase")
                        } //todo:Registrarse con viewmodel y api
                } */
            }
            btnLogin.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("message", "Login Pressed")
                firebaseAnalytics.logEvent("log_in_pressed", bundle)

                navigateToLogin()
            }
            etEmail.afterTextChanged {
                viewModel.validateForm(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etPassword.afterTextChanged {
                viewModel.validateForm(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etConfirmPassword.afterTextChanged {
                viewModel.validateForm(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
                btnSignUp.isEnabled = false
        }
    }
    private fun navigateToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showCorrectDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{
            dialog, id -> navigateToLogin()
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }
}

