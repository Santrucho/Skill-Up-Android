package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.topup.TopupInput
import com.Alkemy.alkemybankbase.data.model.topup.TopupResponse
import com.Alkemy.alkemybankbase.repository.charge.ChargeRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeChargeRepository : ChargeRepository {
    /*
    THE ONLY FUNCTION THAT WILL BE TESTED IS A FORM VALIDATION.
    THE IMPLEMENTATION OF THE FUNCTIONS IN THIS CLASS WILL HAVE
    NO RELATION WITH THE TESTS AS THEY WILL NEVER BE CALLED
     */
    override suspend fun topUp(auth:String, topupInput: TopupInput, id:Int): Resource<TopupResponse> {
        return Resource.Success(TopupResponse())
    }
}