package com.Alkemy.alkemybankbase.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.presentation.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupObservers()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.validateUser(this)
            showToast()
        },5000)
    }

    private fun showToast() {
        Toast.makeText(this, "Timer has finished", Toast.LENGTH_SHORT).show()
    }

    private fun setupObservers(){
        viewModel.isUserLoggedin.observe(this){
            if (it){
                navigateToHome()
            }else{
                navigateToLogin()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}