package com.Alkemy.alkemybankbase.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.presentation.LoginViewModel
import com.Alkemy.alkemybankbase.utils.Constants.GOOGLE_SIGN_IN
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.widget.Toast
import com.Alkemy.alkemybankbase.databinding.ActivityLoginBinding
import com.Alkemy.alkemybankbase.utils.LogBundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAnalytics : FirebaseAnalytics
    //private val callbackManager = CallbackManager.Factory.create()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","login manager")
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","handle")
                handleFacebookAccessToken(loginResult.accessToken)
                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","handle success")
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Succeeded","log_in_success")
            }

            override fun onError(error: FacebookException) {
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Failed","log_in_error")
                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","exception")
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }
        }) */

        setupObservers()
        setupListeners()

        //If the user closed the app while logged in, when opened again he should be taken to home
//        val token = SessionManager.getToken(this)
//        if (!token.isNullOrBlank()) {
//            navigateToHome()
//        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        //auth = FirebaseAuth.getInstance()
    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(this) {
            // enable or disable button
            binding.btnLogin.isEnabled = it
            binding.btnGmail.isEnabled = false
            binding.btnFacebook.isEnabled = false
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
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Pressed","log_in_pressed")
                lifecycleScope.launch {
                    viewModel.loginUser(this@LoginActivity,email = binding.etEmail.text.toString(), password = binding.etPassword.text.toString())
                    if (viewModel.loginResponse.accessToken.isNotBlank()) {
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Succeeded","log_in_success")
                        navigateToHome()

                    } else if(viewModel.loginError.isNotBlank()){
                        //TODO: Show AlertDialog
                        showAlert("Error",viewModel.loginError)
                        LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Failed","log_in_error")
                    }
                }
            }
            /*btnFacebook.setOnClickListener{
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Facebook Pressed","facebook_pressed")

                if (userLoggedIn()){
                    auth.signOut()

                }else{
                    Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","listener else")
                    LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, listOf("email"))
                }
            }
            btnGmail.setOnClickListener{
                LogBundle.logBundleAnalytics(firebaseAnalytics,"Login Gmail Pressed","gmail_pressed")
                var googleClient = viewModel.loginGoogle(this@LoginActivity)
                startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

                /*FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        SessionManager.saveAuthToken(this@LoginActivity, it.result.user?.getIdToken(true).toString())
                        navigateToHome()
                    }else{
                        showAlert("Error","No se pudo autenticar el usuario")
                    }
                }*/
            } */
            btnSignUp.setOnClickListener {
                LogBundle.logBundleAnalytics(firebaseAnalytics,"SignUp Pressed","sign_up_pressed")

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

    }

    /*private fun userLoggedIn(): Boolean {
        if (auth.currentUser != null && AccessToken.getCurrentAccessToken()!!.isExpired){
            Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","userloggin true")
            return true
        }
        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","userloggin false")
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert("Error",e.toString())
            }
        } else{
            Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","1")
            callbackManager.onActivityResult(requestCode, resultCode, data)
            Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@","2")
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this,HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }*/

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