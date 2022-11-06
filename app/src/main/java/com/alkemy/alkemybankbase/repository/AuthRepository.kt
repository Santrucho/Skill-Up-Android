package com.alkemy.alkemybankbase.repository

import com.alkemy.alkemybankbase.data.api.DogsApi.retrofitService
import com.alkemy.alkemybankbase.data.api.apiResponsesStatus
import com.alkemy.alkemybankbase.data.api.dto.LoginDTO
import com.alkemy.alkemybankbase.data.api.dto.SignUpDto
import com.alkemy.alkemybankbase.data.api.dto.UserDtoMapper
import com.alkemy.alkemybankbase.data.api.makeNetworkCall
import com.alkemy.alkemybankbase.data.model.User


class AuthRepository {
//suspend fun se llama para mandar al metodo que esta DENTRO de la corrutina del viewmodel
suspend fun login(email:String, password:String ): apiResponsesStatus<User> = makeNetworkCall{

    val loginDTO = LoginDTO(email,password)
    val loginresponse =  retrofitService.login(loginDTO)

    if(!loginresponse.isSucces){
        throw Exception(loginresponse.message)
    }
    val userDTO  = loginresponse.data.user
    val userDTOMapper = UserDtoMapper()
    userDTOMapper.fromDTOfromMain(userDTO)


}
    suspend fun signUp(email:String, password:String,passwordConfirmation:String): apiResponsesStatus<User> = makeNetworkCall{

        val signUpDto = SignUpDto(email,password,passwordConfirmation)
        val signUpResponse =  retrofitService.signUp(signUpDto)

        if(!signUpResponse.isSucces){
            throw Exception(signUpResponse.message)
        }
        val userDTO  = signUpResponse.data.user
        val userDTOMapper = UserDtoMapper()
        userDTOMapper.fromDTOfromMain(userDTO)


    }

}