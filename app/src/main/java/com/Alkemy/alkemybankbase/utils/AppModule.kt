package com.Alkemy.alkemybankbase.utils

import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.repository.LoginRepository
import com.Alkemy.alkemybankbase.repository.SignUpRepository
import com.Alkemy.alkemybankbase.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {
    @Singleton
    @Provides
    fun provideLoginRepository(apiService: ApiService) = LoginRepository(apiService)

    @Singleton
    @Provides
    fun provideSignupRepository(apiService: ApiService) = SignUpRepository(apiService)

    @Singleton
    @Provides
    fun provideApiService() : ApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }
}