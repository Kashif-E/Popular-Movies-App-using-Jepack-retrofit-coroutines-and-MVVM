package com.infinity.movieapp.ui

import android.app.Application
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


    private val latestMoviesMutable:  MutableLiveData<Resource<List<Result>>>  =
        MutableLiveData()
    val latestMovies: LiveData<Resource<List<Result>>>
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
        getPopularMoviesList()
        getLatestMoviesList()

    }


    private fun getPopularMoviesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.refreshPopularMovieList()
            withContext(Dispatchers.Main) {
                when (response.status) {
                    Status.ERROR -> {
                        withContext(Dispatchers.IO) {
                            handleMoviesResponse(response.message,
                                movieRepository.getPopularMovies(), popularMoviesMutable)
                        }

                    }


                    Status.SUCCESS -> {
                        response.data?.forEach {
                            movieRepository.addMoviesToDb(it)
                        }
                        withContext(Dispatchers.IO) {
                            handleMoviesResponse(response.message,
                                movieRepository.getPopularMovies(), popularMoviesMutable)
                        }
                    }
                    Status.LOADING -> {

                    }

                    else -> {

                        withContext(Dispatchers.IO) {
                            handleMoviesResponse(response.message,
                                movieRepository.getPopularMovies(), popularMoviesMutable)
                        }
                    }
                }
            }
        }

    }
    private fun getLatestMoviesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.refreshlatestMovieList()
            withContext(Dispatchers.Main) {
                when (response.status) {
                    Status.ERROR -> {
                        withContext(Dispatchers.IO) {

                            handleMoviesResponse(response.message,
                                movieRepository.getLatestMovies(), latestMoviesMutable)
                        }

                    }


                    Status.SUCCESS -> {
                        response.data?.forEach {
                            movieRepository.addMoviesToDb(it)
                        }
                        withContext(Dispatchers.IO) {

                            handleMoviesResponse(response.message,
                                movieRepository.getLatestMovies(), latestMoviesMutable)
                        }

                    }
                    Status.LOADING -> {

                    }

                    else -> {

                        withContext(Dispatchers.IO) {

                            handleMoviesResponse(response.message,
                                movieRepository.getLatestMovies(), latestMoviesMutable)
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


    private fun handleMoviesResponse(message: String?, movies: List<ResultDatabaseModel>, list : MutableLiveData<Resource<List<Result>>>) {


        if (movies.asDomainModel().isNotEmpty()) {

            list.postValue(Resource.Success(movies.asDomainModel(),
                responseCode = 0))
        } else {
            list.postValue(Resource.Error(message!!,
                responseCode = 0, data = emptyList()))
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