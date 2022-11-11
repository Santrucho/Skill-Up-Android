package com.Alkemy.alkemybankbase.repository.send

import com.Alkemy.alkemybankbase.data.remote.ApiService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultSendRepository @Inject constructor(private val apiService: ApiService) : SendRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
}