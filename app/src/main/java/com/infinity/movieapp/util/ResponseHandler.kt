package com.infinity.movieapp.util

import android.util.Log
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    NO_INTERNET_CONNECTION,
    NO_DATA_FOUND
}

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500),
    Forbidden(403),
    ServiceUnavailable(503),
    UnAuthorized(401),


}
open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T?, responseCode: Int): Resource<T> {
        return Resource.Success(data, responseCode)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.Error(getErrorMessage(e.code()), null, -5)
            is ConnectivityInterceptor.NoNetworkException -> Resource.NoInternetConnection(
                "No Internet",
                null,
                -1
            )
            is UnknownHostException -> Resource.NoInternetConnection(
                "No Internet",
                null,
                -2
            )
            is ConnectException -> Resource.NoInternetConnection(
                "No Internet",
                null,
                -3
            )
            is SocketTimeoutException -> Resource.Error(
                getErrorMessage(ErrorCodes.SocketTimeOut.code),
                null,
                -4
            )
            else -> Resource.Error(getErrorMessage(Int.MAX_VALUE), null, -1)
        }
    }

    fun <T : Any> handleException(message: String): Resource<T> {
        Log.d("Error","Message$message")
        Log.d("Error","FormattedMessage${getErrorMessage(message.toInt())}")
        return Resource.Error(getErrorMessage(message.toInt()), null, -1)
    }

    fun <T : Any> handleException(statusCode: Int): Resource<T> {
        return Resource.Error(getErrorMessage(statusCode), null, -1)
    }
}
private fun getErrorMessage(code: Int): String {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> "Time Out"
        ErrorCodes.UnAuthorized.code -> "Un Authorised"

        ErrorCodes.InternalServerError.code -> "Internal Server Error"

        ErrorCodes.BadRequest.code -> "Bad Request"

        ErrorCodes.InternalServerError.code -> "Internal Server Error"
        ErrorCodes.NotFound.code -> "Not Found"
        ErrorCodes.ServiceUnavailable.code ->"Service Unavailable"
        ErrorCodes.Forbidden.code -> "Forbidden"
        else -> "Something Went Wrong"
    }

}
