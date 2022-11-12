package com.Alkemy.alkemybankbase.data.remote

import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.utils.Resource
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users")
    suspend fun addUser(@Body user: User) : UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginInput: LoginInput) : LoginResponse

    @POST("transactions")
    suspend fun transaction(@Body send: Send) : SendResponse
}