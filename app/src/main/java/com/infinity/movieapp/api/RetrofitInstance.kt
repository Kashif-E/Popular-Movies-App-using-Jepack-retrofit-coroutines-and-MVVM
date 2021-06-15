package com.infinity.movieapp.api

import com.infinity.movieapp.util.Constants.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            logging.setLevel((HttpLoggingInterceptor.Level.BODY))
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client).build()


        }

        val api by lazy {
            retrofit.create(api::class.java)
        }
    }
}