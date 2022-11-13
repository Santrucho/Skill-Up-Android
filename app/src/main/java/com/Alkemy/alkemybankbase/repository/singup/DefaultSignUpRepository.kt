package com.Alkemy.alkemybankbase.repository.singup

import com.Alkemy.alkemybankbase.data.model.user.User
import com.Alkemy.alkemybankbase.data.model.user.UserResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped

import javax.inject.Inject

@ActivityScoped
class DefaultSignUpRepository @Inject constructor(private val apiService:ApiService) : SignUpRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun createUser(user: User): Resource<UserResponse> {
        val resp = try {
            apiService.addUser(user)
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
        return Resource.Success(resp)
    }
}

