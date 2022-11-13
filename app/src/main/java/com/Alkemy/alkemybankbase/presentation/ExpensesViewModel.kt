package com.Alkemy.alkemybankbase.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Alkemy.alkemybankbase.data.local.AccountManager
import com.Alkemy.alkemybankbase.data.model.expense.Expense
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expensesRepo: ExpensesRepository
    ) : ViewModel() {

    val amountErrorResourceLiveData = MutableLiveData<Int>()
    val conceptErrorResourceLiveData = MutableLiveData<String>()
    val dateErrorResourceIdLiveData = MutableLiveData<String>()
    val toAccountIdErrorResourceIdLiveData = MutableLiveData<Int>()
    val isFormValidLiveData = MutableLiveData<Boolean>()

    lateinit var expenseResponse: Transaction
    val errorLiveData = MutableLiveData<String>() //Error adding expense
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun validateForm(concept:String,amount:Int,date:String,currency:String){
        //TODO: Quien haga esta implementacion recuerde que currency no la vamos a usar, y que faltan parametros ahi
    }

    fun addExpense(context: Context, auth: String, amount: Int, concept: String, date: String, to_account_id: Int) {
        isLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                val expense = Expense(
                    amount,
                    concept,
                    date,
                    "payment",
                    AccountManager.getAccountId(context)!!.toInt(),
                    AccountManager.getUserId(context)!!.toInt(),
                    to_account_id
                )
                expensesRepo.addExpense(auth, expense)
            }
            when (response) {
                is Resource.Failure -> {
                    isLoadingLiveData.value = false
                    errorLiveData.value = response.toString()
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
