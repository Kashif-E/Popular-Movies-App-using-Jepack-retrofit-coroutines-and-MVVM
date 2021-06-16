package com.infinity.movieapp.repository

import com.infinity.movieapp.MovieApplicationClass
import com.infinity.movieapp.api.RetrofitInstance
import com.infinity.movieapp.database.MovieDatabase
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel
import com.infinity.movieapp.models.netwrokmodels.asDataBaseModel
import com.infinity.movieapp.util.Resource

class MovieRepository(private val db: MovieDatabase) {


    suspend fun refreshPopularMovieList(): Resource<List<ResultDatabaseModel>> {

        return try {

            val response = RetrofitInstance.api.getPopularMoviesAsync()

            if (response.isSuccessful) {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleSuccess(response.body()!!.results.asDataBaseModel(popularMovies = true),
                        response.code())
            } else {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleException(response.errorBody()!!.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MovieApplicationClass.getInstance().getResponseHandler().handleException(e)
        }

    }

    suspend fun refreshlatestMovieList(): Resource<List<ResultDatabaseModel>> {

        return try {

            val response = RetrofitInstance.api.getLatestMoviesAsync()

            if (response.isSuccessful) {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleSuccess(response.body()!!.results.asDataBaseModel(latestMovies = true),
                        response.code())
            } else {
                MovieApplicationClass.getInstance().getResponseHandler()
                    .handleException(response.errorBody()!!.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MovieApplicationClass.getInstance().getResponseHandler().handleException(e)
        }

    }


    suspend fun addMoviesToDb(movie: ResultDatabaseModel) = db.getMovieDAO().upsert(movie)
    fun getPopularMovies() = db.getMovieDAO().getAllPopularMovies()
    fun getSavedMovies() = db.getSavedMoviesDao().getAllSavedMovie()
    suspend fun deleteMovie(movie: SavedResultDatabaseModel) =
        db.getSavedMoviesDao().deleteMovie(movie)

    suspend fun upsertSavedMoviesToDb(movie: SavedResultDatabaseModel) =
        db.getSavedMoviesDao().upsert(movie)

    fun getLatestMovies() = db.getMovieDAO().getAlllatestMovies()

}

