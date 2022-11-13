package com.Alkemy.alkemybankbase.data.model.expense

data class ExpenseResponse (
    var amount: Int = 0,
    var concept: String = "",
    var date: String = "",
    var type: String = "",
    var accountId: Int = 0,
    var userId: Int = 0,
    var to_account_id: Int = 0
){
}