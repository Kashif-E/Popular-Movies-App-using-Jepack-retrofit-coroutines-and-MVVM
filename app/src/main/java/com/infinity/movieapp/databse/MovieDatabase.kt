package com.infinity.movieapp.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infinity.movieapp.models.Result

@Database
    (
    entities = [Result::class],
    version = 1
)

abstract class MovieDatabase : RoomDatabase(){

    abstract  fun getMovieDAO() : MoviesDao

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
                "article_db.db"
            ).build()
    }
}