package com.Alkemy.alkemybankbase.repository.expense

import com.Alkemy.alkemybankbase.data.remote.ApiService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultExpensesRepository @Inject constructor(private val apiService: ApiService) : ExpensesRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
}