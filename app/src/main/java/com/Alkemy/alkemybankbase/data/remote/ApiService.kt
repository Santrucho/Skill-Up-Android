package com.Alkemy.alkemybankbase.data.remote

import com.Alkemy.alkemybankbase.data.model.LoginInput
import com.Alkemy.alkemybankbase.data.model.LoginResponse
import com.Alkemy.alkemybankbase.data.model.User
import com.Alkemy.alkemybankbase.data.model.UserResponse
import com.Alkemy.alkemybankbase.utils.Resource
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users")
    suspend fun addUser(@Body user: User) : UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginInput: LoginInput) : LoginResponse
}