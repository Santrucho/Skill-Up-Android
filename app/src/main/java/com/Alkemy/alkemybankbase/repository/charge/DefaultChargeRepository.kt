package com.Alkemy.alkemybankbase.repository.charge

import com.Alkemy.alkemybankbase.data.remote.ApiService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultChargeRepository @Inject constructor(private val apiService: ApiService) : ChargeRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/

}