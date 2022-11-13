package com.Alkemy.alkemybankbase.repository.account

import com.Alkemy.alkemybankbase.data.model.*
import com.Alkemy.alkemybankbase.data.model.account.NewAccount
import com.Alkemy.alkemybankbase.data.model.account.NewAccountResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultAccountRepository @Inject constructor(private val apiService: ApiService) : AccountRepository{
    override suspend fun getAllAccounts(auth: String): Resource<AccountsResponse> {
        val resp = try {
            apiService.getAllAccounts(auth)
        }catch (e:Exception){
            return  Resource.Failure(e)
        }
        return Resource.Success(resp)
    }

    override suspend fun createAccount(auth: String,newAccount: NewAccount): Resource<NewAccountResponse> {
        val resp = try{
            apiService.createNewAccount(auth, newAccount)
        }catch(e:Exception){
            return Resource.Failure(e)
        }
        return Resource.Success(resp)
    }

}