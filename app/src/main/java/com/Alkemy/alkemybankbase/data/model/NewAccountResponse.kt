package com.Alkemy.alkemybankbase.data.model

data class NewAccountResponse(
    val createdAt: String,
    val creationDate: String,
    val id: Int,
    val isBlocked: Boolean,
    val money: Int,
    val updatedAt: String,
    val userId: Int
)
