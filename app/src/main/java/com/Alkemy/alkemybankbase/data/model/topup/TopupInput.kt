package com.Alkemy.alkemybankbase.data.model.topup

import com.google.gson.annotations.SerializedName

data class TopupInput(
    @SerializedName("type")
    var type:String,
    @SerializedName("concept")
    var concept:String,
    @SerializedName("amount")
    var amount:Int = 0
) {
}