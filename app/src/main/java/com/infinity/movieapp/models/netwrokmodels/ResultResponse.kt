package com.infinity.movieapp.models.netwrokmodels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ResultResponse(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "backdrop_path")
    val backdrop_path: String,
    @field:Json(name = "original_title")
    val original_title: String,
    @field:Json(name ="overview" )
    val overview: String,
    @field:Json(name ="popularity" )
    val popularity: String,
    @field:Json(name ="poster_path" )
    val poster_path: String,
    @field:Json(name ="release_date" )
    val release_date: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "vote_averag")
    val vote_average: String,
)