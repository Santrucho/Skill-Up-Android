package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel(){
    val isUserLoggedin = MutableLiveData<Boolean>()

    fun validateUser(context: Context){
        val token = SessionManager.getToken(context)
        isUserLoggedin.value = !token.isNullOrBlank()


    }
}