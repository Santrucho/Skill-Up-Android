package com.Alkemy.alkemybankbase.repository.movement

import com.Alkemy.alkemybankbase.data.model.TransactionsResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultMovementRepository @Inject constructor(private val apiService: ApiService) :
    MovementRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun getAllTransactions(auth: String): Resource<TransactionsResponse> {
        val resp = try {
            apiService.getAllTransactions(auth)
        }catch (e:Exception){
            return  Resource.Failure(e)
        }
        return Resource.Success(resp)
    }
}