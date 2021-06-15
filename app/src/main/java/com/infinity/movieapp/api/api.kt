package com.infinity.movieapp.api


import com.infinity.movieapp.models.netwrokmodels.PopularMoviesResponse

import com.infinity.movieapp.util.Constants.Companion.API_KEY
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface api {

   @GET("/3/movie/popular")
   suspend fun getPopularMovies(
       @Query("api_key")
       api_key: String = API_KEY
   ) : Response<PopularMoviesResponse>

    @GET("/3/movie/top_rated")
    suspend fun getLatestMovies(
        @Query("api_key")
        api_key: String = API_KEY
    ) : Response <PopularMoviesResponse>

}