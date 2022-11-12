package com.Alkemy.alkemybankbase.data.model

data class SendResponse(
    val amount: String = "",
    var concept : String = "",
    var date : String = "",
    var type : String = "",
    var accountId : String = "",
    var userId : String = "",
    var to_account_id : String = ""
)