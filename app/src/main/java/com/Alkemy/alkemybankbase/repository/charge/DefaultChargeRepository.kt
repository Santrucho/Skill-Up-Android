package com.Alkemy.alkemybankbase.repository.charge

import com.Alkemy.alkemybankbase.data.model.topup.TopupInput
import com.Alkemy.alkemybankbase.data.model.topup.TopupResponse
import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultChargeRepository @Inject constructor(private val apiService: ApiService) : ChargeRepository {
    /***********************************************************
    IMPLEMENTATIONS GO HERE, MAKE SURE TO USE OVERRIDE
     ************************************************************/
    override suspend fun topUp(auth:String, topupInput: TopupInput, id:Int): Resource<TopupResponse> {
        val resp = try {
            apiService.chargeBalance(auth,topupInput,id)
        }catch (e:Exception){
            return  Resource.Failure(e)
        }
        return Resource.Success(resp)
    }

}