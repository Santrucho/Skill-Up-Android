package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.repository.send.SendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(private val sendRepo : SendRepository) : ViewModel() {

    fun validateForm(toAccount_id:Int,concept:String,amount:Int,date:String,currency:String,password:String,confirmPassword:String){

    }
}
