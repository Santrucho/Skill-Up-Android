package com.Alkemy.alkemybankbase.data.model

data class NewAccount(
    val userId: Int,
    val creationDate: String,
    val money: Int = 0,
    val isBlocked: Boolean = false
)
