package com.infinity.movieapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel

@Database
    (
    entities = [ResultDatabaseModel::class,SavedResultDatabaseModel::class ],
    version = 2
)

abstract class MovieDatabase : RoomDatabase(){

    abstract  fun getMovieDAO() : MoviesDao
    abstract fun getSavedMoviesDao() : SavedMoviesDao

    companion object
    {
        @Volatile
        private  var instance : MovieDatabase? = null
        private  val Lock = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(Lock){
            instance?: createDatabase(context).also { instance = it }
        }

        private  fun createDatabase(context: Context) =
            Room.databaseBuilder(

                context.applicationContext,
                MovieDatabase::class.java,
                "movie_db.db"
            ).build()
    }
}