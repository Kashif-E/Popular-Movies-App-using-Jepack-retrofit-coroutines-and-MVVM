package com.infinity.movieapp.util

import androidx.lifecycle.asLiveData
import com.infinity.movieapp.MovieApplicationClass
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException


open class ConnectivityInterceptor : Interceptor {

    val state =
        NetworkStatusTracker(MovieApplicationClass.getInstance().baseContext).networkStatus
            .map(
                onAvailable = { MyState.Fetched },
                onUnavailable = { MyState.Error }
            )
            .asLiveData(Dispatchers.IO)

    private val isConnected: Boolean
        get() {
            return when(state.value){
                is MyState.Fetched ->{
                    return true
                }
                is MyState.Error->{
                    return false
                }
                else -> false
            }
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