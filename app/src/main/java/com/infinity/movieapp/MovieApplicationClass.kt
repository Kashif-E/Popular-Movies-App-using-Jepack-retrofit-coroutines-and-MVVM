package com.infinity.movieapp

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.work.*
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.ResponseHandler
import com.infinity.movieapp.worker.RefreshMoviesWorker
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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


    private var responseHandler: ResponseHandler? = null
    fun getResponseHandler(): ResponseHandler {
        synchronized(MovieApplicationClass::class.java) {
            if (responseHandler == null)
                responseHandler = ResponseHandler()
        }
        return responseHandler!!

    }

    override fun onCreate() {
        super.onCreate()
        setupWorker()
    }

    private fun setupWorker() {
        CoroutineScope(Dispatchers.IO).launch {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
               /* .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // setRequiresDeviceIdle(true)
                    }
                }*/.build()
            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<RefreshMoviesWorker>(repeatInterval = 1,
                    repeatIntervalTimeUnit = TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshMoviesWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
}