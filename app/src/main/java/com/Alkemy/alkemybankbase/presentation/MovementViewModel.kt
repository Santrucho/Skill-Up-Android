package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.model.Account
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.repository.movement.MovementRepository
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
    val allAccountLiveData = MutableLiveData<List<Account>>()
    val errorLiveData = MutableLiveData<Int>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    fun getAllTransactions(auth: String) {
        isLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                movementRepo.getAllTransactions(auth)
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
                    allTransactionLiveData.value = response.data.transactions

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

    fun retry(auth: String) {
        getAllAccounts(auth)
        getAllTransactions(auth)

    }
}