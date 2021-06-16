package com.infinity.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie : ResultDatabaseModel): Long

    @Query("SELECT * FROM movies WHERE popular")
    fun getAllPopularMovies():List<ResultDatabaseModel>
    @Query("SELECT * FROM movies WHERE latest")
    fun getAlllatestMovies():List<ResultDatabaseModel>


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