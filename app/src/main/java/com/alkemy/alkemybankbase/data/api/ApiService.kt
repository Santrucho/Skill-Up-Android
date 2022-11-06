package com.alkemy.alkemybankbase.data.api

import com.alkemy.alkemybankbase.data.api.dto.LoginDTO
import com.alkemy.alkemybankbase.data.api.dto.SignUpDto
import com.alkemy.alkemybankbase.data.api.responses.AuthApiResponse
 import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


private val retrofit =  Retrofit.Builder()
        .baseUrl("https://todogs.herokuapp.com/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

interface ApiService{

        @POST("sign_in")
        suspend fun  login(@Body signInDto: LoginDTO): AuthApiResponse

        @POST("sign_up")
        suspend fun  signUp(@Body signUpDto: SignUpDto): AuthApiResponse

}


object DogsApi{
        val retrofitService: ApiService by lazy {
                retrofit.create(ApiService::class.java)
        }
}
