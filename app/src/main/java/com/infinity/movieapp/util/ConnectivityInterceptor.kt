package com.infinity.movieapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.infinity.movieapp.MovieApplicationClass
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException


open class ConnectivityInterceptor : Interceptor {

    private val isConnected: Boolean
        get() {
            return isInternetAvailable(MovieApplicationClass.getInstance())
        }



    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!isConnected) {
            throw NoNetworkException()
        }
        return chain.proceed(originalRequest)
    }

    class NoNetworkException internal constructor() :
        IOException(Status.NO_INTERNET_CONNECTION.toString())
}