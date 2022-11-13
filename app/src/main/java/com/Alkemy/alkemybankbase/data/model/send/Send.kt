package com.Alkemy.alkemybankbase.data.model.send
import com.google.gson.annotations.SerializedName

data class Send (
    @SerializedName("amount")
    var amount: Int,
    @SerializedName("concept")
    var concept : String,
    @SerializedName("type")
    var type : String
)