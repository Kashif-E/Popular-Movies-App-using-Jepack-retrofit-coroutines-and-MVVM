package com.infinity.movieapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.SavedResultDatabaseModel
import com.infinity.movieapp.models.databasemodels.asDomainModel
import com.infinity.movieapp.models.databasemodels.toDomainModel
import com.infinity.movieapp.models.domainmodel.Result
import com.infinity.movieapp.models.netwrokmodels.PopularMoviesResponse
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.Resource
import com.infinity.movieapp.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieViewModel(app: Application, private val movieRepository: MovieRepository) :
    AndroidViewModel(app) {


    private val latestMoviesMutable: MutableLiveData<Resource<PopularMoviesResponse>> =
        MutableLiveData()
    val latestMovies: LiveData<Resource<PopularMoviesResponse>>
        get() = latestMoviesMutable
    private val savedMoviesMutable: MutableLiveData<Resource<List<Result>>> =
        MutableLiveData()
    val savedMovies: LiveData<Resource<List<Result>>>
        get() = savedMoviesMutable
    private val popularMoviesMutable: MutableLiveData<Resource<List<Result>>> =
        MutableLiveData()
    val popularMovies: LiveData<Resource<List<Result>>>
        get() = popularMoviesMutable

    init {
        refreshList()

    }


    private fun refreshList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.refreshMovieList()
            withContext(Dispatchers.Main) {
                when (response.status) {
                    Status.ERROR -> {
                        withContext(Dispatchers.IO) {

                            handleMoviesResponse(response.message,
                                movieRepository.getPopularMovies())
                        }

                    }


                    Status.SUCCESS -> {
                        response.data?.forEach {
                            movieRepository.addMoviesToDb(it)
                        }
                        withContext(Dispatchers.IO) {
                            handleMoviesResponse(response.message,
                                movieRepository.getPopularMovies())
                        }
                    }
                    Status.LOADING -> {

                    }

                    else -> {

                        withContext(Dispatchers.IO) {

                            handleMoviesResponse("Something Went Wrong",
                                movieRepository.getPopularMovies())
                        }
                    }
                }
            }
        }

    }


/*    private fun getLatestMovies() = viewModelScope.launch {


            latestMoviesMutable.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                val response = movieRepository.getLatestMovies()
                latestMoviesMutable.postValue(handleMoviesResponse(response))
            } else {
                latestMoviesMutable.postValue(Resource.Error("No Internet Connection"))
            }



    }*/


    private fun handleMoviesResponse(message: String?, popularMovies: List<ResultDatabaseModel>) {

        popularMoviesMutable.postValue(Resource.Loading(emptyList(),
            responseCode = 0))

        if (popularMovies.asDomainModel().isNotEmpty()) {

            popularMoviesMutable.postValue(Resource.Success(popularMovies.asDomainModel(),
                responseCode = 0))
        } else {
            popularMoviesMutable.postValue(Resource.Error(message!!,
                responseCode = 0,
                data = null))
        }

    }


    fun saveMovie(movie: SavedResultDatabaseModel) = viewModelScope.launch {
        movieRepository.upsertSavedMoviesToDb(movie)
    }

    fun getSavedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMovies = movieRepository.getSavedMovies()

            savedMoviesMutable.postValue(Resource.Success(savedMovies.toDomainModel() as MutableList<Result>,
                0))
        }
    }


    fun deleteMovie(movie: SavedResultDatabaseModel) = viewModelScope.launch(Dispatchers.IO) {

        movieRepository.deleteMovie(movie)
        movieRepository.getSavedMovies()


    }


}