package com.infinity.movieapp.repository

import android.util.Log
import com.infinity.movieapp.MovieApplicationClass
import com.infinity.movieapp.api.RetrofitInstance
import com.infinity.movieapp.database.MovieDatabase
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel
import com.infinity.movieapp.models.netwrokmodels.asDataBaseModel
import com.infinity.movieapp.util.Resource
import com.infinity.movieapp.util.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRepository(private val db: MovieDatabase) {


    suspend fun refreshPopularMovieList(): Resource<List<ResultDatabaseModel>> {

        return try {

            val response = RetrofitInstance.api.getPopularMoviesAsync()

            if (response.isSuccessful) {
                val result = MovieApplicationClass.getInstance().getResponseHandler()
                    .handleSuccess(response.body()!!.results.asDataBaseModel(popularMovies = true),
                        response.code())
                handleResult(result)
                return result
            } else {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleException(response.errorBody()!!.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MovieApplicationClass.getInstance().getResponseHandler().handleException(e)
        }

    }

    suspend fun refreshLatestMovieList(): Resource<List<ResultDatabaseModel>> {
        val response = RetrofitInstance.api.getLatestMoviesAsync()
        return try {
            if (response.isSuccessful) {
                val result = MovieApplicationClass.getInstance().getResponseHandler()
                    .handleSuccess(response.body()!!.results.asDataBaseModel(latestMovies = true),
                        response.code())
                handleResult(result)
                return result
            } else {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleException(response.errorBody().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MovieApplicationClass.getInstance().getResponseHandler().handleException(e)
        }

    }

    private fun handleResult(result: Resource<List<ResultDatabaseModel>>) {

        CoroutineScope(Dispatchers.Default).launch {
            when (result.status) {
                Status.ERROR -> {
                    withContext(Dispatchers.IO) {
                        Log.e("NETWORK ERROR", result.message.toString())
                    }

                }
                Status.SUCCESS -> {

                    addMoviesToDb(result.data!!)


                }
                Status.LOADING -> {

                    Log.e("NETWORK LOADING", result.message.toString())
                }
                else -> {
                    Log.e("NETWORK ERROR", result.message.toString())
                }
            }
        }


    }


    private suspend fun addMoviesToDb(movie: List<ResultDatabaseModel>) =
        db.getMovieDAO().upsert(movie)

    fun getPopularMovies() = db.getMovieDAO().getAllPopularMovies()
    fun getSavedMovies() = db.getSavedMoviesDao().getAllSavedMovie()
    suspend fun deleteMovie(movie: SavedResultDatabaseModel) =
        db.getSavedMoviesDao().deleteMovie(movie)

    suspend fun upsertSavedMoviesToDb(movie: SavedResultDatabaseModel) =
        db.getSavedMoviesDao().upsert(movie)

    fun getLatestMovies() = db.getMovieDAO().getAlllatestMovies()

}

