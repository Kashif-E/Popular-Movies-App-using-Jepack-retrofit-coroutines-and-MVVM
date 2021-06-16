package com.infinity.movieapp.models.netwrokmodels

import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultResponse(
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "backdrop_path")
    val backdrop_path: String?,
    @field:Json(name = "original_title")
    val original_title: String?,
    @field:Json(name ="overview" )
    val overview: String?,
    @field:Json(name ="popularity" )
    val popularity: String?,
    @field:Json(name ="poster_path" )
    val poster_path: String?,
    @field:Json(name ="release_date" )
    val release_date: String?,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "vote_average")
    val vote_average: String?
)
fun List<ResultResponse>.asDataBaseModel(): List<ResultDatabaseModel> {
    return map {
       ResultDatabaseModel(
           id = it.id!!,
           backdrop_path = it.backdrop_path!!,
           original_title = it.original_title!!,
           overview = it.overview!!,
           popularity = it.popularity!!,
           poster_path = it.poster_path!!,
           release_date = it.release_date!!,
           title = it.title!!,
           vote_average = it.vote_average!!

       )
    }
}

