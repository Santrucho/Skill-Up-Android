package com.Alkemy.alkemybankbase.repository.account

import com.Alkemy.alkemybankbase.data.model.AccountsResponse
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
}