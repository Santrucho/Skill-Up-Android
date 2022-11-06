package com.alkemy.alkemybankbase.data.api.dto

import com.squareup.moshi.Json

class SignUpDto (email:String,
                 password:String,
                 @field:Json(name = "password_confirmation") val passwordConfirmation:String)