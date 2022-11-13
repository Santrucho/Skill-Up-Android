package com.Alkemy.alkemybankbase.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Failure<T>(message: Exception, data: T? = null) : Resource<T>(data, message)
}
/*
Made a few changes to the class:
-Loading most of the times won't contain data but it could happen.
-Failure in particular does not return an exception. It returns a message
or sometimes a data class made of an error message and a status code.
 */