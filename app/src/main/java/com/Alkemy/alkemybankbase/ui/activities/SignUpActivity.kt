package com.Alkemy.alkemybankbase.ui.activities


import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.databinding.ActivitySignUpBinding
import com.Alkemy.alkemybankbase.presentation.SignUpViewModel
import com.Alkemy.alkemybankbase.utils.LogBundle
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
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
        setupListener()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(this) {
            // enable or disable button
            binding.btnSignUp.isEnabled = it
        }
        viewModel.firstnameErrorResourceLiveData.observe(this){ resId ->
            //show firstname error
            binding.etFirstname.error = getString(resId)
        }
        viewModel.lastnameErrorResourceLiveData.observe(this){ resId ->
            //show lastname error
            binding.etLastname.error = getString(resId)
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
            binding.prgbar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.isAccountCreated.observe(this) {
            // call after creating the account
            if(it)
                showDialog("Great!","User was successfully registered")
        }
        viewModel.token.observe(this) {
            // create account
            val  auth = "Bearer $it"
            viewModel.createAccount(auth)
        }
    }

    private fun setupListener() {
        with(binding) {
            btnSignUp.setOnClickListener {
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Sign Up Pressed","register_pressed")
                lifecycleScope.launch {
                    viewModel.createUser(etFirstname.text.toString(),etLastname.text.toString(),etEmail.text.toString(),etPassword.text.toString())
                    if (viewModel.userResponse.email.isNotBlank()) {
                        //login the user to have token
                        viewModel.getToken(etEmail.text.toString(), etPassword.text.toString())
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Sign Up Succeeded","sign_up_success")
                    } else if(viewModel.userError.isNotBlank()){
                        showAlert("Error",viewModel.userError)
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Sign Up Failed","sign_up_error")

                    }
                }
            }
            btnLogin.setOnClickListener {
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Pressed","log_in_pressed")
                navigateToLogin()
            }
            etFirstname.afterTextChanged {
                viewModel.validateForm(
                    etFirstname.text.toString(),
                    etLastname.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etLastname.afterTextChanged {
                viewModel.validateForm(
                    etFirstname.text.toString(),
                    etLastname.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etEmail.afterTextChanged {
                viewModel.validateForm(
                    etFirstname.text.toString(),
                    etLastname.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etPassword.afterTextChanged {
                viewModel.validateForm(
                    etFirstname.text.toString(),
                    etLastname.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
            etConfirmPassword.afterTextChanged {
                viewModel.validateForm(
                    etFirstname.text.toString(),
                    etLastname.text.toString(),
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

    private fun showDialog(title: String,message: String){
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){
            dialog,id -> navigateToLogin()
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(title:String,message:String) {
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

