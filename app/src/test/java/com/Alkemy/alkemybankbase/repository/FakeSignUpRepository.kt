package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.User
import com.Alkemy.alkemybankbase.data.model.UserResponse
import com.Alkemy.alkemybankbase.repository.singup.SignUpRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeSignUpRepository : SignUpRepository {
    /*
    THE ONLY FUNCTION THAT WILL BE TESTED IS A FORM VALIDATION.
    THE IMPLEMENTATION OF THE FUNCTIONS IN THIS CLASS WILL HAVE
    NO RELATION WITH THE TESTS AS THEY WILL NEVER BE CALLED
    */
    override suspend fun createUser(user: User): Resource<UserResponse> {
        return Resource.Success(UserResponse())
    }
}