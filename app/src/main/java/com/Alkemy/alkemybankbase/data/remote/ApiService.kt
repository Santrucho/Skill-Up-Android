package com.Alkemy.alkemybankbase.data.remote

import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.data.model.account.NewAccount
import com.Alkemy.alkemybankbase.data.model.account.NewAccountResponse
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseInput
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseResponse
import com.Alkemy.alkemybankbase.data.model.login.LoginInput
import com.Alkemy.alkemybankbase.data.model.login.LoginResponse
import com.Alkemy.alkemybankbase.data.model.topup.TopupInput
import com.Alkemy.alkemybankbase.data.model.topup.TopupResponse
import com.Alkemy.alkemybankbase.data.model.user.User
import com.Alkemy.alkemybankbase.data.model.user.UserResponse
import retrofit2.http.*

interface ApiService {

    @POST("users")
    suspend fun addUser(@Body user: User): UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginInput: LoginInput): LoginResponse

    @GET("transactions")
    suspend fun getAllTransactions(@Header("Authorization") auth: String): TransactionsResponse

    @GET("accounts/me")
    suspend fun getAllAccounts(@Header("Authorization") auth: String): AccountsResponse

    @POST("accounts")
    suspend fun createNewAccount(
        @Header("Authorization") auth: String,
        @Body newAccount: NewAccount
    ): NewAccountResponse

    @POST("transactions")
    suspend fun addExpense(
        @Header("Authorization") auth: String,
        @Body expense: ExpenseInput
    ): ExpenseResponse

    @POST("accounts/{id}")
    suspend fun chargeBalance(@Header("Authorization")auth:String, @Body topupInput: TopupInput, @Path("id")id:Int) : TopupResponse
}