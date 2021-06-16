package com.infinity.movieapp.util


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */

sealed class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val responseCode: Int,
) {

    class Success<T>(data: T?, responseCode: Int) : Resource<T>(
        Status.SUCCESS,
        data,
        null,
        responseCode
    )

    class Error<T>(msg: String, data: T?, responseCode: Int) : Resource<T>(
        Status.ERROR,
        data,
        msg,
        responseCode
    )

    class NoInternetConnection<T>(msg: String, data: T?, responseCode: Int) : Resource<T>(
        Status.NO_INTERNET_CONNECTION,
        data,
        msg,
        responseCode
    )

    class Loading<T>(data: T?, responseCode: Int) : Resource<T>(
        Status.LOADING,
        data,
        null,
        responseCode
    )
}



