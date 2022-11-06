package com.alkemy.alkemybankbase.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.alkemy.alkemybankbase.MainActivity
import com.alkemy.alkemybankbase.R
import com.alkemy.alkemybankbase.data.api.apiResponsesStatus
import com.alkemy.alkemybankbase.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() , LoginFragment.LoginFragmentActions, SignUpFragment.SignUpFragmentActions{
private val viewmodel: AuthViewModel by viewModels()
private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.status.observe(this)

        {       status->
            when(status)
            {
                is apiResponsesStatus.ERROR ->
                {
                     binding.loadingWheel.visibility = View.GONE

                    showErrorDialog(status.message)

                }
                is apiResponsesStatus.LOADING -> binding.loadingWheel.visibility = View.VISIBLE
                is apiResponsesStatus.SUCCESS ->  binding.loadingWheel.visibility = View.GONE

            }
        }

        viewmodel.user.observe(this)
        {
            user ->
            if(user != null){
                startMainActivity()
            }
        }

    }

    private fun startMainActivity(){
startActivity(Intent(this@LoginActivity,MainActivity::class.java))
    }
    private fun showErrorDialog(message: String){
        AlertDialog.Builder(this)
            .setTitle("Tienes un error")
            .setMessage(message)
            .setPositiveButton("ok"){_,_ -> }
            .create()
            .show()
    }



    override fun onRegisterButton() {
        findNavController(R.id.nav_host_fragment).navigate(
            LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginFieldValidate(email: String, password: String) {
viewmodel.login(email,password)

    }

    override fun onSignUpFieldValidate(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
viewmodel.signUp(email,password,passwordConfirmation)

    }
}