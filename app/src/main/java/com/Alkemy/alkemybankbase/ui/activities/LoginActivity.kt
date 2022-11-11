package com.Alkemy.alkemybankbase.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.databinding.ActivityLoginBinding
import com.Alkemy.alkemybankbase.presentation.LoginViewModel
import com.Alkemy.alkemybankbase.utils.Constants.GOOGLE_SIGN_IN
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.widget.Toast
import com.facebook.appevents.AppEventsLogger;
import com.Alkemy.alkemybankbase.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAnalytics : FirebaseAnalytics
    lateinit var binding: ActivityLoginBinding
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var auth: FirebaseAuth
    var TAG = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()

        //If the user closed the app while logged in, when opened again he should be taken to home
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(this) {
            // enable or disable button
            binding.btnLogin.isEnabled = it
            binding.btnGmail.isEnabled = it
            binding.btnFacebook.isEnabled = it
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
    }

    private fun setupListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                //When clicked, sends the event to firebase analytics
                var bundle = Bundle()
                bundle.putString("message", "Login Pressed")
                firebaseAnalytics.logEvent("log_in_pressed", bundle)
                lifecycleScope.launch {
                    viewModel.loginUser(this@LoginActivity,email = binding.etEmail.text.toString(), password = binding.etPassword.text.toString())
                    if (viewModel.loginResponse.accessToken.isNotBlank()) {
                        bundle.putString("message", "Login Succeeded")
                        firebaseAnalytics.logEvent("log_in_success", bundle)
                        navigateToHome()
                    } else if(viewModel.loginError.isNotBlank()){
                        //TODO: Show AlertDialog
                        showAlert("Error",viewModel.loginError)
                        bundle.putString("message", "Login Failed")
                        firebaseAnalytics.logEvent("log_in_error", bundle)
                    }
                }
            }
            btnFacebook.setOnClickListener{
                var bundle = Bundle()
                bundle.putString("message", "Login Facebook Pressed")
                firebaseAnalytics.logEvent("facebook_pressed", bundle)

            }
            btnGmail.setOnClickListener{
                var bundle = Bundle()
                bundle.putString("message", "Login Gmail Pressed")
                firebaseAnalytics.logEvent("gmail_pressed", bundle)
                var googleClient = viewModel.loginGoogle(this@LoginActivity)
                startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        SessionManager.saveAuthToken(this@LoginActivity, it.result.user?.getIdToken(true).toString())
                        navigateToHome()
                    }else{
                        showAlert("Error","No se pudo autenticar el usuario")
                    }
                }
            }
            btnSignUp.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("message", "SignUp Pressed")
                firebaseAnalytics.logEvent("sign_up_pressed", bundle)

                val signIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(signIntent)
            }
            etEmail.afterTextChanged {
                viewModel.validateForm(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
            etPassword.afterTextChanged {
                viewModel.validateForm(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
            btnLogin.isEnabled = false
            btnGmail.isEnabled = false
            btnFacebook.isEnabled = false
        }
        
        auth = FirebaseAuth.getInstance()
        binding.btnFacebook.setOnClickListener{
            if (userLoggedIn()){
                auth.signOut()

            }else{
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            }
        }
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }

    private fun userLoggedIn(): Boolean {
        if (auth.currentUser != null && AccessToken.getCurrentAccessToken()!!.isExpired){
            return true
        }
        return false
    }

    override fun onActivityResultFacebook(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this,HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    override fun onActivityResultGoogle(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            navigateToHome()
                        }else{
                            showAlert("Error","No se pudo autenticar el usuario")
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert("Error",e.toString())
            }
        }

    }
    private fun showAlert(title:String, message:String){
        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToHome(){
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }

}