package com.Alkemy.alkemybankbase.data.model.expense

import com.google.gson.annotations.SerializedName

data class Expense (
    @SerializedName("amount") val amount: Int,
    @SerializedName("concept")  val concept: String,
    @SerializedName("date")  val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("accountId") val accountId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("to_account_id")  val toAccountId: Int
    ){}
