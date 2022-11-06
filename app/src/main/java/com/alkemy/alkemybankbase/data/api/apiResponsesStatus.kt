package com.alkemy.alkemybankbase.data.api

sealed class apiResponsesStatus<T> {

    class SUCCESS<T>(val data: T): apiResponsesStatus<T>()
    class LOADING<T>:    apiResponsesStatus<T>()
    class ERROR<T>(val message: String): apiResponsesStatus<T>()


}