package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.send.Send
import com.Alkemy.alkemybankbase.data.model.send.SendResponse
import com.Alkemy.alkemybankbase.repository.send.SendRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeSendRepository : SendRepository {
    /*
    THE ONLY FUNCTION THAT WILL BE TESTED IS A FORM VALIDATION.
    THE IMPLEMENTATION OF THE FUNCTIONS IN THIS CLASS WILL HAVE
    NO RELATION WITH THE TESTS AS THEY WILL NEVER BE CALLED
     */
    override suspend fun sendUser(auth: String, send: Send, id:Int): Resource<SendResponse> {
        return Resource.Success(SendResponse())
    }
}