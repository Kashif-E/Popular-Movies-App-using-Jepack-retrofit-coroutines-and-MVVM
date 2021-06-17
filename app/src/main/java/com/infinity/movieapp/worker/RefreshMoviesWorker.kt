package com.infinity.movieapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinity.movieapp.database.MovieDatabase
import com.infinity.movieapp.repository.MovieRepository
import retrofit2.HttpException

class RefreshMoviesWorker(val context: Context , params : WorkerParameters) : CoroutineWorker(context, params) {
    companion object{
      const val  WORK_NAME = "Refresh Movies"
    }
    override suspend fun doWork(): Result {

        val database = MovieDatabase.invoke(context = context)
        val repo = MovieRepository(database)

        return  try {
            repo.refreshPopularMovieList()
            repo.refreshLatestMovieList()
            Result.success()
        }catch (e : HttpException){
                Result.failure()
        }
    }
}