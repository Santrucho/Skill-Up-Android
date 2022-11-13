package com.Alkemy.alkemybankbase.repository.expense

import com.Alkemy.alkemybankbase.data.model.expense.ExpenseInput
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultExpensesRepository @Inject constructor(private val apiService: ApiService) : ExpensesRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun addExpense(auth: String, expense: ExpenseInput): Resource<ExpenseResponse> {
        val resp = try {
            apiService.addExpense(auth,expense)
        }catch (e:Exception){
            return  Resource.Failure(e.message.toString())
        }
        return Resource.Success(resp)
    }
}