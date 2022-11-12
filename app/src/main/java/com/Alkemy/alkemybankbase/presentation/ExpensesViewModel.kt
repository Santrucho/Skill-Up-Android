package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.repository.movement.MovementRepository
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expensesRepo : ExpensesRepository,
    private val accountRepo: AccountRepository
    ) : ViewModel() {

    val amountErrorResourceLiveData = MutableLiveData<Int>()
    val conceptErrorResourceLiveData = MutableLiveData<String>()
    val dateErrorResourceIdLiveData = MutableLiveData<String>()
    val toAccountIdErrorResourceIdLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    lateinit var expenseResponse: Transaction
    val allAccountLiveData = MutableLiveData<List<Account>>()
    val errorLiveData = MutableLiveData<Int>() //Error getting accounts
    val errorExpenseLiveData = MutableLiveData<String>() //Error adding expense
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun validateForm(concept:String,amount:Int,date:String,currency:String){
        //TODO: Quien haga esta implementacion recuerde que currency no la vamos a usar, y que faltan parametros ahi
    }

    fun addExpense(auth: String, amount: Int, concept: String, date: String, to_account_id: Int) {
        isLoadingLiveData.value = true
        getAllAccounts(auth)
        if (!allAccountLiveData.value.isNullOrEmpty())
        {
            viewModelScope.launch(Dispatchers.Main) {
                val response = withContext(Dispatchers.IO) {
                    val expense = Expense(
                        amount,
                        concept,
                        date,
                        "payment",
                        allAccountLiveData.value!!.first().id,
                        allAccountLiveData.value!!.first().userId,
                    to_account_id)
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

    fun getAllAccounts(auth: String){
        isLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.Main){
            val response = withContext(Dispatchers.IO){
                accountRepo.getAllAccounts(auth)
            }
            when(response){
                is Resource.Failure -> {
                    isLoadingLiveData.value = false
                    errorLiveData.value = R.string.no_internet

                }
                is Resource.Loading -> {

                }
                is Resource.Success ->{
                    isLoadingLiveData.value = false
                    allAccountLiveData.value = response.data ?: emptyList()
                }
            }
        }
    }
}
