package com.Alkemy.alkemybankbase.repository.account


import com.Alkemy.alkemybankbase.data.model.AccountsResponse
import com.Alkemy.alkemybankbase.data.model.account.NewAccount
import com.Alkemy.alkemybankbase.data.model.account.NewAccountResponse
import com.Alkemy.alkemybankbase.utils.Resource

interface AccountRepository {

    suspend fun getAllAccounts(auth: String) : Resource<AccountsResponse>
    suspend fun createAccount(auth: String, newAccount: NewAccount): Resource<NewAccountResponse>
}