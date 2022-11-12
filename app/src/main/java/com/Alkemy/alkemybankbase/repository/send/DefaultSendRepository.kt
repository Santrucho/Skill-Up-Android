package com.Alkemy.alkemybankbase.repository.send

import com.Alkemy.alkemybankbase.data.model.Send
import com.Alkemy.alkemybankbase.data.model.SendResponse
import com.Alkemy.alkemybankbase.data.model.User
import com.Alkemy.alkemybankbase.data.model.UserResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultSendRepository @Inject constructor(private val apiService: ApiService) : SendRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun sendUser(send: Send): Resource<SendResponse> {
        val resp = try {
            apiService.transaction(send)
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
        return Resource.Success(resp)
    }
}