package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.local.AccountManager
import com.Alkemy.alkemybankbase.data.model.topup.TopupInput
import com.Alkemy.alkemybankbase.data.model.topup.TopupResponse
import com.Alkemy.alkemybankbase.repository.charge.ChargeRepository
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_TOPUP
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ChargeViewModel @Inject constructor(private val chargeRepo : ChargeRepository) : ViewModel() {

    val amountErrorResourceLiveData = MutableLiveData<Int>()
    val conceptErrorResourceLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    lateinit var topUpResponse: TopupResponse
    val errorLiveData = MutableLiveData<String>() //Error adding topUp
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun validateForm(amount:Int,concept:String){
        val amountValid = amount>=1
        // check if concept is valid with pattern
        val conceptPattern = "[a-zA-Z][a-zA-Z ]*"
        val patternFn = Pattern.compile(conceptPattern)
        val isConceptValid = patternFn.matcher(concept).matches()
        if (!amountValid){
            amountErrorResourceLiveData.value = R.string.amount_error
            isFormValidLiveData.value = false
        }else if(!isConceptValid || concept.length !in 1..19){
            conceptErrorResourceLiveData.value = R.string.concept_error
            isFormValidLiveData.value = false
        }else{
            isFormValidLiveData.value = true
        }
    }

    suspend fun topUp(context: Context, auth:String, amount:Int, concept:String){

        var topUpResult: Resource<TopupResponse>
        isLoadingLiveData.value = true
        val topUpInput = TopupInput(TYPE_TOPUP,concept,amount)

        topUpResult = chargeRepo.topUp(auth,topUpInput,AccountManager.getAccountId(context)!!.toInt())
        topUpResponse = TopupResponse()

        when(topUpResult){
            is Resource.Success -> {
                isLoadingLiveData.value = false
                topUpResponse = topUpResult.data!!
            }
            is Resource.Failure -> {
                errorLiveData.value = topUpResult.message.toString()
                isLoadingLiveData.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }
}
