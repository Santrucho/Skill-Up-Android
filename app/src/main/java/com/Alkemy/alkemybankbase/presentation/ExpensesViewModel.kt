package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expensesRepo: ExpensesRepository,
    private val accountRepo: AccountRepository
) : ViewModel() {

    val amountErrorResourceLiveData = MutableLiveData<String>()
    val conceptErrorLiveData = MutableLiveData<String>()
//    val dateErrorResourceIdLiveData = MutableLiveData<String>()
//    val toAccountIdErrorResourceIdLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    lateinit var expenseResponse: Transaction
    val allAccountLiveData = MutableLiveData<List<Account>>()
    val errorLiveData = MutableLiveData<Int>() //Error getting accounts
    val errorExpenseLiveData = MutableLiveData<String>() //Error adding expense
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun validateForm(concept: String, amount: Int, /*date: String, currency: String*/) {

        val conceptPattern = "[a-zA-Z][a-zA-Z ]*"
        val patternFn = Pattern.compile(conceptPattern)
        val isConceptValid = patternFn.matcher(concept).matches()
           if(!isConceptValid ){
               conceptErrorLiveData.value = "Please enter a valid concept"
               isFormValidLiveData.value = false
           }else if(amount <= 0){
               amountErrorResourceLiveData.value = "Please enter a valid amount"
               isFormValidLiveData.value = false
           }else{
               isFormValidLiveData.value = true
               conceptErrorLiveData.value = null
               amountErrorResourceLiveData.value = null
           }
    }

    fun addExpense(auth: String, amount: Int, concept: String, date: String, to_account_id: Int) {
        isLoadingLiveData.value = true
        getAllAccounts(auth)
        if (!allAccountLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.Main) {
                val response = withContext(Dispatchers.IO) {
                    val expense = Expense(
                        amount,
                        concept,
                        date,
                        "payment",
                        allAccountLiveData.value!!.first().id,
                        allAccountLiveData.value!!.first().userId,
                        to_account_id
                    )
                    expensesRepo.addExpense(auth, expense)
                }
                when (response) {
                    is Resource.Failure -> {
                        isLoadingLiveData.value = false
                        errorExpenseLiveData.value = response.toString()
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        isLoadingLiveData.value = false
                        expenseResponse = response.data
                    }
                }
            }
        }
    }

    fun getAllAccounts(auth: String) {
        isLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                accountRepo.getAllAccounts(auth)
            }
            when (response) {
                is Resource.Failure -> {
                    isLoadingLiveData.value = false
                    errorLiveData.value = R.string.no_internet

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    isLoadingLiveData.value = false
                    allAccountLiveData.value = response.data ?: emptyList()
                }
            }
        }
    }
}
