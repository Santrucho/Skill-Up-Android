package com.Alkemy.alkemybankbase.data.remote

import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.utils.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users")
    suspend fun addUser(@Body user: User) : UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginInput: LoginInput) : LoginResponse

    @GET("transactions")
    suspend fun getAllTransactions(@Header("Authorization") auth: String) : TransactionsResponse

    @GET("accounts/me")
    suspend fun getAllAccounts(@Header("Authorization") auth: String): AccountsResponse

    @POST("transactions")
    suspend fun addExpense(@Header("Authorization") auth: String): Transaction
}