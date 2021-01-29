package com.infinity.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity.movieapp.models.Result

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie : Result): Long

    @Query("SELECT * FROM movies")
    fun getAllMovie(): LiveData<List<Result>>

    @Delete
    suspend fun deleteMovie(movie: Result)
}