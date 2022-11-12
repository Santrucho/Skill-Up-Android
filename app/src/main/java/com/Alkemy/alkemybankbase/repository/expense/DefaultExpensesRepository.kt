package com.Alkemy.alkemybankbase.repository.expense

import com.Alkemy.alkemybankbase.data.model.Expense
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultExpensesRepository @Inject constructor(private val apiService: ApiService) : ExpensesRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun addExpense(auth: String, expense: Expense): Resource<Transaction> {
        val resp = try {
            apiService.addExpense(auth)
        }catch (e:Exception){
            return  Resource.Failure(e)
        }
        return Resource.Success(resp)
    }
}