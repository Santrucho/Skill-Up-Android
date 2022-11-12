package com.Alkemy.alkemybankbase.data.model

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(
    @SerializedName("data") val transactions: List<Transaction>,
    @SerializedName("nextPage") val nextPage: Any,
    @SerializedName("previousPage") val previousPage: Any
)

data class Transaction(
    @SerializedName("accountId") val accountId: Int,
    @SerializedName("amount") val amount: String,
    @SerializedName("concept")  val concept: String,
    @SerializedName("createdAt")  val createdAt: String,
    @SerializedName("date")  val date: String,
    @SerializedName("id")  val id: Int,
    @SerializedName("to_account_id")  val toAccountId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("userId") val userId: Int
)
