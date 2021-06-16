package com.infinity.movieapp.models.databasemodels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import com.infinity.movieapp.models.domainmodel.Result
import com.infinity.movieapp.models.netwrokmodels.ResultResponse
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName ="movies"
    )
@Parcelize
data class ResultDatabaseModel(

    @PrimaryKey
    val id: String,
    val backdrop_path: String,
    val original_title: String,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: String,
) : Parcelable

fun List<ResultDatabaseModel>.asDomainModel(): List<Result> {
    return map {
        Result(
            id = it.id,
            backdrop_path = it.backdrop_path,
            original_title = it.original_title,
            overview = it.overview,
            popularity = it.popularity,
            poster_path = it.poster_path,
            release_date = it.release_date,
            title = it.title,
            vote_average = it.vote_average

        )
    }
}

@Entity(
    tableName ="saved_movies"
)
@Parcelize
data class SavedResultDatabaseModel(

    @PrimaryKey
    val id: String,
    val backdrop_path: String,
    val original_title: String,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: String,
) : Parcelable

fun List<SavedResultDatabaseModel>.toDomainModel(): List<Result> {
    return map {
        Result(
            id = it.id,
            backdrop_path = it.backdrop_path,
            original_title = it.original_title,
            overview = it.overview,
            popularity = it.popularity,
            poster_path = it.poster_path,
            release_date = it.release_date,
            title = it.title,
            vote_average = it.vote_average

        )
    }
}