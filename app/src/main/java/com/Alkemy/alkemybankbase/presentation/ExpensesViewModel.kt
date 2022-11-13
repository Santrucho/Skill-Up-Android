package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.local.AccountManager
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseInput
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseResponse
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_PAYMENT
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expensesRepo: ExpensesRepository
    ) : ViewModel() {

    val amountErrorResourceLiveData = MutableLiveData<Int>()
    val conceptErrorResourceLiveData = MutableLiveData<Int>()
    val toAccountIdErrorResourceLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    lateinit var expenseResponse: ExpenseResponse
    val errorLiveData = MutableLiveData<String>() //Error adding expense
    val statusLiveData = MutableLiveData<Int>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun validateForm(concept: String, amount: Int, toAccountId: Int) {
        val conceptPattern = "[a-zA-Z][a-zA-Z ]*"
        val patternFn = Pattern.compile(conceptPattern)
        val isConceptValid = patternFn.matcher(concept).matches()
        if(!isConceptValid || concept.length !in 1..19 ){
            conceptErrorResourceLiveData.value = R.string.concept_error
            isFormValidLiveData.value = false
        }else if(amount <= 0){
            amountErrorResourceLiveData.value = R.string.amount_error
            isFormValidLiveData.value = false
        }else if(toAccountId <= 0){
            toAccountIdErrorResourceLiveData.value = R.string.toAccountId_error
        }
        else{
            isFormValidLiveData.value = true
        }
    }

    suspend fun addExpense(context: Context, auth: String, amount: Int, concept: String, to_account_id: Int) {
        isLoadingLiveData.value = true
        val expenseInput = ExpenseInput(
            amount,
            concept,
            LocalDateTime.now().toString(),
            TYPE_PAYMENT,
            AccountManager.getAccountId(context)!!.toInt(),
            AccountManager.getUserId(context)!!.toInt(),
            to_account_id
        )
        expenseResponse = ExpenseResponse()
        when(val expenseResult = expensesRepo.addExpense(auth, expenseInput)){
            is Resource.Success -> {
                errorLiveData.value = ""
                isLoadingLiveData.value = false
                expenseResponse = expenseResult.data!!
            }
            is Resource.Failure -> {
                errorLiveData.value = expenseResult.message.toString()
                isLoadingLiveData.value = false
            }
            else -> throw IllegalArgumentException("Illegal Result")
        }
    }
}
