package com.infinity.movieapp.models.netwrokmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PopularMoviesResponse(
    @field:Json(name ="page" )
    val page: Int,
    @field:Json(name = "result")
    val resultResponses: List<ResultResponse>,
    @field:Json(name ="total_pages" )
    val total_pages: Int,
    @field:Json(name ="total_results" )
    val total_results: Int
)