package com.infinity.movieapp.models.domainmodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName ="movies"
    )
@Parcelize
@JsonClass(generateAdapter = true)
data class Result(

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