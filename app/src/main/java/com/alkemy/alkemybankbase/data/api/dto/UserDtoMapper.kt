package com.alkemy.alkemybankbase.data.api.dto

import com.alkemy.alkemybankbase.data.model.User


class UserDtoMapper  {
    fun fromDTOfromMain(userDTO: UserDTO ): User {
        return User(userDTO.id, userDTO.email,userDTO.autenticationToken )
    }


}