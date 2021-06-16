package com.infinity.movieapp.models.netwrokmodels

import com.infinity.movieapp.models.databasemodels.PopularMoviesDatabaseModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PopularMoviesResponse(
    @field:Json(name ="page" )
    var page: Int,
    @field:Json(name = "results")
    val results: List<ResultResponse>,
    @field:Json(name ="total_pages" )
    val total_pages: Int,
    @field:Json(name ="total_results" )
    val total_results: Int
)






