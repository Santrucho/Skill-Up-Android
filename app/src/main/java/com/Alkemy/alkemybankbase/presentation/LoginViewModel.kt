package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.data.model.LoginInput
import com.Alkemy.alkemybankbase.data.model.LoginResponse
import com.Alkemy.alkemybankbase.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import java.util.regex.Pattern
import javax.inject.Inject
import com.Alkemy.alkemybankbase.repository.login.LoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepo : LoginRepository) : ViewModel() {
    val emailErrorResourceIdLiveData = MutableLiveData<Int>()
    val passwordErrorResourceIdLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()
    lateinit var loginResponse : LoginResponse
    var loginError : String = ""
    val isLoading = MutableLiveData<Boolean>()


    //Check email & password
    fun validateForm(email: String, password: String) {
        // check if email is valid with pattern
        val isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        // check if password is valid with pattern
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+\$).{8,}"
        val pattern = Pattern.compile(passwordPattern)
        val isPasswordValid = pattern.matcher(password).matches()

        if (!isEmailValid){
            emailErrorResourceIdLiveData.value = R.string.email_error
            isFormValidLiveData.value = false
        }else if (!isPasswordValid){
            passwordErrorResourceIdLiveData.value = R.string.password_error
            isFormValidLiveData.value = false
        }else{
            isFormValidLiveData.value = true
        }
    }

    suspend fun loginUser(context: Context, email:String, password:String){
        var loginResult: Resource<LoginResponse>
        isLoading.value = true
        val loginInput = LoginInput(email = email,
            password = password)
        loginResult = loginRepo.loginUser(loginInput = loginInput)
        loginResponse = LoginResponse()
        when(loginResult){
            is Resource.Success -> {
                loginResponse = loginResult.data
                SessionManager.saveAuthToken(context, loginResult.data.accessToken)
                isLoading.value = false
            }
            is Resource.Failure -> {
                loginError = loginResult.toString()
                isLoading.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }

    fun loginGoogle(context:Context) : GoogleSignInClient{
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken((R.string.default_web_client_id.toString()))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(context, googleConf)
        googleClient.signOut()
        return googleClient
        //startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
    }
}