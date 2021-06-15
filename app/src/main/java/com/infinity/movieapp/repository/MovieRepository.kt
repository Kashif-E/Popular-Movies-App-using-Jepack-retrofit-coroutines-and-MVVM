package com.infinity.movieapp.repository

import com.infinity.movieapp.api.RetrofitInstance
import com.infinity.movieapp.database.MovieDatabase
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel

class MovieRepository(private val db : MovieDatabase) {

    suspend fun  getPopularMovies() = RetrofitInstance.api.getPopularMovies()
    suspend fun  getLatestMovies() = RetrofitInstance.api.getLatestMovies()
    suspend fun upsert (movie: ResultDatabaseModel) = db.getMovieDAO().upsert(movie)
    fun getSavedMovie() = db.getMovieDAO().getAllMovie()
    suspend fun  deleteMovie(movie: ResultDatabaseModel) = db.getMovieDAO().deleteMovie(movie)

}