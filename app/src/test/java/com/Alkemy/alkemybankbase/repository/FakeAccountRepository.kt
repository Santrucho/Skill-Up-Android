package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.AccountsResponse
import com.Alkemy.alkemybankbase.data.model.account.NewAccount
import com.Alkemy.alkemybankbase.data.model.account.NewAccountResponse
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeAccountRepository : AccountRepository {
    override suspend fun getAllAccounts(auth: String): Resource<AccountsResponse> {
        return Resource.Success(AccountsResponse())
    }

    override suspend fun createAccount(auth: String,newAccount: NewAccount): Resource<NewAccountResponse> {
        return Resource.Success(NewAccountResponse("","",0,false,0,"",0))
    }
}