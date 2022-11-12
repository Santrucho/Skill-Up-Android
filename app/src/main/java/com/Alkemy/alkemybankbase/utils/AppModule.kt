package com.Alkemy.alkemybankbase.utils

import com.Alkemy.alkemybankbase.data.remote.ApiService
import com.Alkemy.alkemybankbase.repository.*
import com.Alkemy.alkemybankbase.repository.account.AccountRepository
import com.Alkemy.alkemybankbase.repository.account.DefaultAccountRepository
import com.Alkemy.alkemybankbase.repository.charge.ChargeRepository
import com.Alkemy.alkemybankbase.repository.charge.DefaultChargeRepository
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.repository.expense.DefaultExpensesRepository
import com.Alkemy.alkemybankbase.repository.login.LoginRepository
import com.Alkemy.alkemybankbase.repository.login.DefaultLoginRepository
import com.Alkemy.alkemybankbase.repository.movement.DefaultMovementRepository
import com.Alkemy.alkemybankbase.repository.movement.MovementRepository
import com.Alkemy.alkemybankbase.repository.send.SendRepository
import com.Alkemy.alkemybankbase.repository.send.DefaultSendRepository
import com.Alkemy.alkemybankbase.repository.singup.SignUpRepository
import com.Alkemy.alkemybankbase.repository.singup.DefaultSignUpRepository
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
    fun provideLoginRepository(apiService: ApiService) = DefaultLoginRepository(apiService) as LoginRepository

    @Singleton
    @Provides
    fun provideSignUpRepository(apiService: ApiService) = DefaultSignUpRepository(apiService) as SignUpRepository

    @Singleton
    @Provides
    fun provideChargeRepository(apiService: ApiService) = DefaultChargeRepository(apiService) as ChargeRepository

    @Singleton
    @Provides
    fun provideSendRepository(apiService: ApiService) = DefaultSendRepository(apiService) as SendRepository

    @Singleton
    @Provides
    fun provideExpensesRepository(apiService: ApiService) = DefaultExpensesRepository(apiService) as ExpensesRepository

    @Singleton
    @Provides
    fun provideMovementRepository(apiService: ApiService) = DefaultMovementRepository(apiService) as MovementRepository

    @Singleton
    @Provides
    fun provideAccountRepository(apiService: ApiService) = DefaultAccountRepository(apiService) as AccountRepository

    @Singleton
    @Provides
    fun provideApiService() : ApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }
}