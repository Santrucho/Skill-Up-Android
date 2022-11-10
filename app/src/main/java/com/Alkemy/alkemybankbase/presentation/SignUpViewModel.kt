package com.Alkemy.alkemybankbase.presentation

import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.repository.SignUpRepo
import com.Alkemy.alkemybankbase.data.model.User
import com.Alkemy.alkemybankbase.data.model.UserResponse
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signupRepo : SignUpRepo) : ViewModel() {

    val firstnameErrorResourceLiveData = MutableLiveData<Int>()
    val lastnameErrorResourceLiveData = MutableLiveData<Int>()
    val emailErrorResourceIdLiveData = MutableLiveData<Int>()
    val passwordErrorResourceIdLiveData = MutableLiveData<Int>()
    val confirmPasswordErrorResourceIdLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()
    lateinit var userResponse : UserResponse
    var userError : String = ""
    val isLoading = MutableLiveData<Boolean>()

    //Check email & password
    fun validateForm(firstname:String,lastname:String,email: String, password: String, confirmPassword: String) {
        // check if firstname is valid with pattern
        val firstnamePattern = "^[\\w'-,.][^0-9_!¡?÷?¿/\\+=@#\$%ˆ&*(){}|~<>;:[ ]]{2,}\$"
        val patternFn = Pattern.compile(firstnamePattern)
        val isFirstnameValid = patternFn.matcher(firstname).matches()
        // check if lastname is valid with pattern
        val lastnamePattern = "^[\\w'-,.][^0-9_!¡?÷?¿/\\+=@#\$%ˆ&*(){}|~<>;:[ ]]{2,}\$"
        val patternLn = Pattern.compile(lastnamePattern)
        val isLastnameValid = patternLn.matcher(lastname).matches()
        // check if email is valid with pattern
        val isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        // check if password is valid with pattern
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+\$).{8,}"
        val pattern = Pattern.compile(passwordPattern)
        val isPasswordValid = pattern.matcher(password).matches()
        // check if passwords are the same
        val isConfirmPasswordValid = password == confirmPassword

        if(!isFirstnameValid){
            firstnameErrorResourceLiveData.value = R.string.firstname_error
            isFormValidLiveData.value = false
        }
        else if(!isLastnameValid){
            lastnameErrorResourceLiveData.value = R.string.lastname_error
            isFormValidLiveData.value = false
        }
        else if (!isEmailValid){
            emailErrorResourceIdLiveData.value = R.string.email_error
            isFormValidLiveData.value = false
        }else if (!isPasswordValid){
            passwordErrorResourceIdLiveData.value = R.string.password_error
            isFormValidLiveData.value = false
        }else if (!isConfirmPasswordValid){
            confirmPasswordErrorResourceIdLiveData.value = R.string.confirm_password_error
            isFormValidLiveData.value = false
        }else{
            isFormValidLiveData.value = true
        }

    }

    suspend fun createUser(firstname:String, lastname:String,email:String,password:String){
        var userResult: Resource<UserResponse>
        isLoading.value = true
        val user = User(firstname = firstname, lastname = lastname, email = email,
            password = password)
        userResult = signupRepo.createUser(user =  user)
        userResponse = UserResponse()
        when(userResult){
            is Resource.Success -> {
                userResponse = userResult.data
                //SessionManager.saveAuthToken(context, userResult.data.accessToken)
                isLoading.value = false
            }
            is Resource.Failure -> {
                userError = userResult.toString()
                isLoading.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }

}

