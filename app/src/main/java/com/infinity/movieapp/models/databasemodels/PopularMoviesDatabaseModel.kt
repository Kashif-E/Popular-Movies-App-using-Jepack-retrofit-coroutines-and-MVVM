package com.infinity.movieapp.models.databasemodels

data class PopularMoviesDatabaseModel(
    val page: Int,
    val results: List<ResultDatabaseModel>,
    val total_pages: Int,
    val total_results: Int
)