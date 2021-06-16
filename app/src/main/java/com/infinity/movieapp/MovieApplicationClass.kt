package com.infinity.movieapp

import android.app.Application
import android.widget.Toast
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.ResponseHandler
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieApplicationClass : Application() {

    init {
        instance = this

    }

    companion object {

        private var instance: MovieApplicationClass? = null
        fun getInstance(): MovieApplicationClass {
            synchronized(MovieApplicationClass::class.java) {
                if (instance == null)
                    instance =
                        MovieApplicationClass()
            }
            return instance!!
        }
    }
    private var moshi: Moshi? = null
    fun getMoshi(): Moshi {
        synchronized(MovieApplicationClass::class.java) {
            if (moshi == null)
                moshi = Moshi.Builder().build()
        }
        return moshi!!

    }

    fun showToast(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
             Toast.makeText(instance, message, Toast.LENGTH_SHORT).show()

        }


    }
    private var responseHandler: ResponseHandler? = null
    fun getResponseHandler(): ResponseHandler {
        synchronized(MovieApplicationClass::class.java) {
            if (responseHandler == null)
                responseHandler = ResponseHandler()
        }
        return responseHandler!!

    }
}