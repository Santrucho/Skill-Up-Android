package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.login.LoginInput
import com.Alkemy.alkemybankbase.data.model.login.LoginResponse
import com.Alkemy.alkemybankbase.repository.login.LoginRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeLoginRepository : LoginRepository {
    /*
    THE ONLY FUNCTION THAT WILL BE TESTED IS A FORM VALIDATION.
    THE IMPLEMENTATION OF THE FUNCTIONS IN THIS CLASS WILL HAVE
    NO RELATION WITH THE TESTS AS THEY WILL NEVER BE CALLED
    */
    override suspend fun loginUser(loginInput: LoginInput): Resource<LoginResponse> {
        return Resource.Success(LoginResponse())
    }
}