package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.data.model.Send
import com.Alkemy.alkemybankbase.data.model.SendResponse
import com.Alkemy.alkemybankbase.data.model.User
import com.Alkemy.alkemybankbase.data.model.UserResponse
import com.Alkemy.alkemybankbase.repository.send.SendRepository
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(private val sendRepo : SendRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    lateinit var sendResponse : SendResponse
    var sendError : String = ""



    fun validateForm(toAccount_id:Int,concept:String,amount:Int,date:String,currency:String){

    }

    suspend fun send(
        toAccount_id: String,
        concept:String,
        amount:String,
        date:String,
        type:String, userId:String,
        accountId: String){
        var sendResult: Resource<SendResponse>
        isLoading.value = true
        val send = Send(amount = amount,date = date, to_account_id = toAccount_id, concept = concept, type = type, userId = userId, accountId = accountId )
        sendResult = sendRepo.sendUser(send)
        sendResponse = SendResponse()
        when(sendResult){
            is Resource.Success -> {
                //SessionManager.saveAuthToken(context, userResult.data.accessToken)
                isLoading.value = false
                sendResponse = sendResult.data
            }
            is Resource.Failure -> {
                sendError = sendResult.toString()
                isLoading.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }
}
