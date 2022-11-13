package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.local.AccountManager
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseResponse
import com.Alkemy.alkemybankbase.data.model.send.Send
import com.Alkemy.alkemybankbase.data.model.send.SendResponse
import com.Alkemy.alkemybankbase.repository.send.SendRepository
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_PAYMENT
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(private val sendRepo : SendRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    lateinit var sendResponse : SendResponse
    var sendError : String = ""
    val amountErrorResourceLiveData = MutableLiveData<Int>()
    val conceptErrorResourceLiveData = MutableLiveData<Int>()
    val toAccountIdErrorResourceLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    fun validateForm(toAccount_Id:Int,concept:String,amount:Int){
        // check if concept is valid with pattern
        val conceptPattern = "[a-zA-Z][a-zA-Z ]*"
        val patternFn = Pattern.compile(conceptPattern)
        val isConceptValid = patternFn.matcher(concept).matches()
        if(!isConceptValid || concept.length !in 1..19){
            conceptErrorResourceLiveData.value = R.string.concept_error
            isFormValidLiveData.value = false
        }else if(amount <= 0){
            amountErrorResourceLiveData.value = R.string.amount_error
            isFormValidLiveData.value = false
        }else if(toAccount_Id <= 0){
            toAccountIdErrorResourceLiveData.value = R.string.toAccountId_error
        }
        else{
            isFormValidLiveData.value = true
        }
    }
    suspend fun send(
        auth:String,
        toAccount_id: Int,
        concept:String,
        amount:Int){
        var sendResult: Resource<SendResponse>
        isLoading.value = true
        val send = Send(amount, concept, TYPE_PAYMENT)
        sendResult = sendRepo.sendUser(auth, send, toAccount_id)
        sendResponse = SendResponse()
        when(sendResult){
            is Resource.Success -> {
                //SessionManager.saveAuthToken(context, userResult.data.accessToken)
                isLoading.value = false
                sendResponse = sendResult.data!!
            }
            is Resource.Failure -> {
                sendError = sendResult.toString()
                isLoading.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }
}
