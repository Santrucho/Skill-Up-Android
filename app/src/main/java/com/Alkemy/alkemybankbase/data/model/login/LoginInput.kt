package com.Alkemy.alkemybankbase.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginInput(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String
) {
}