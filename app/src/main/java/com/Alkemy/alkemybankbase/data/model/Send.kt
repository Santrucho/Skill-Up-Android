package com.Alkemy.alkemybankbase.data.model

import com.google.gson.annotations.SerializedName
import java.time.temporal.TemporalAmount

data class Send (
    @SerializedName("amount")
    var amount: String,
    @SerializedName("concept")
    var concept : String,
    @SerializedName("date")
    var date : String,
    @SerializedName("type")
    var type : String,
    @SerializedName("accountId")
    var accountId : String,
    @SerializedName("userId")
    var userId : String,
    @SerializedName("to_account_id")
    var to_account_id : String)