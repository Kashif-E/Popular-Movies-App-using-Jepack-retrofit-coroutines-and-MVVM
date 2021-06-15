package com.infinity.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie : ResultDatabaseModel): Long

    @Query("SELECT * FROM movies")
    fun getAllMovie(): LiveData<List<ResultDatabaseModel>>

    @Delete
    suspend fun deleteMovie(movie: ResultDatabaseModel)
}