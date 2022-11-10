package com.Alkemy.alkemybankbase.data.model

data class UserResponse(
    val code : Int = 0,
    val id : Int = 0,
    val firstname : String = "",
    val lastname : String = "",
    val email : String = "",
    val password : String = "",
) {
}