package com.Alkemy.alkemybankbase.repository.movement

import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultMovementRepository @Inject constructor(private val apiService: ApiService) :
    ExpensesRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
}