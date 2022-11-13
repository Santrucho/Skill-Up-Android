package com.Alkemy.alkemybankbase.data.model

import com.google.gson.annotations.SerializedName

class AccountsResponse : ArrayList<Account>()

data class Account(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("creationDate")  val creationDate: String,
    @SerializedName("id")  val id: Int,
    @SerializedName("isBlocked") val isBlocked: Boolean,
    @SerializedName("money") val money: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("userId")  val userId: Int
)