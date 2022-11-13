package com.Alkemy.alkemybankbase.repository.send

import com.Alkemy.alkemybankbase.data.model.send.Send
import com.Alkemy.alkemybankbase.data.model.send.SendResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultSendRepository @Inject constructor(private val apiService: ApiService) : SendRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun sendUser(auth: String, send: Send, id:Int): Resource<SendResponse> {
        val resp = try {
            apiService.sendBalance(auth, send, id)
        } catch (e: Exception) {
            return Resource.Failure(e.message.toString())
        }
        return Resource.Success(resp)
    }
}