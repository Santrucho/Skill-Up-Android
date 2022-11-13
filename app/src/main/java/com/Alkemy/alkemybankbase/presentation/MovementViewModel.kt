package com.Alkemy.alkemybankbase.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.model.Account
import com.Alkemy.alkemybankbase.data.model.AccountsResponse
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.repository.movement.MovementRepository
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_PAYMENT
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_TOPUP
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovementViewModel @Inject constructor(
    private val movementRepo: MovementRepository,
    private val accountRepo: AccountRepository
) : ViewModel() {
    val allTransactionLiveData = MutableLiveData<List<Transaction>>()
    val balance = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<Int>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun getAllTransactions(auth: String, account: Account? = null) {
        isLoadingLiveData.value = true
        balance.value = 0.toString()
        viewModelScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                movementRepo.getAllTransactions(auth)
            }
            when (response) {
                is Resource.Failure -> {
                    isLoadingLiveData.value = false
                    errorLiveData.value = R.string.no_internet
                    Log.d("88888888888888888888888888888888888888888","error")

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    isLoadingLiveData.value = false
                    val transactionList = response.data.transactions
                    //calculate the balance
                    var payments = 0
                    var topUp = 0
                    transactionList.filter { it.type == TYPE_PAYMENT }.forEach { trans ->
                        payments += trans.amount.toIntOrNull() ?: 0
                    }
                    Log.d("oooooooooooooooooooooooooooooooooooooooooooooooooo",payments.toString())
                    transactionList.filter { it.type == TYPE_TOPUP }.forEach { trans ->
                        topUp += trans.amount.toIntOrNull() ?: 0
                    }
                    Log.d("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY",topUp.toString())
                    account?.let {
                        val newBalance = topUp - payments
                        Log.d("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",newBalance.toString())
                        balance.value = newBalance.toString()
                    }
                    Log.d("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM",balance.value.toString())
                    allTransactionLiveData.value = transactionList

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
                    Log.d("9999999999999999999999999999999999999999999999","error")
                }
                is Resource.Loading -> {

                }
                is Resource.Success ->{
                    isLoadingLiveData.value = false
                    getAllTransactions(auth, response.data.firstOrNull())
                }
            }
        }
    }

    fun retry(auth: String) {
        getAllAccounts(auth)

    }
}