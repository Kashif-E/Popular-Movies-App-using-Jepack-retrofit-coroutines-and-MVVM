package com.infinity.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert( movie :List<ResultDatabaseModel>): List<Long>

    @Query("SELECT * FROM movies WHERE popular")
    fun getAllPopularMovies():LiveData<List<ResultDatabaseModel>>
    @Query("SELECT * FROM movies WHERE latest")
    fun getAlllatestMovies():LiveData<List<ResultDatabaseModel>>


    @Delete
    suspend fun deleteMovie(movie: ResultDatabaseModel)
}


@Dao
interface SavedMoviesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie : SavedResultDatabaseModel): Long


    @Query("SELECT * FROM saved_movies")
    fun getAllSavedMovie(): List<SavedResultDatabaseModel>

    @Delete
    suspend fun deleteMovie(movie: SavedResultDatabaseModel)
}