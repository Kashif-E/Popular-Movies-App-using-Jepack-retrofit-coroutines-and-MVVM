package com.infinity.movieapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinity.movieapp.MovieApplicationClass
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import com.infinity.movieapp.models.netwrokmodels.PopularMoviesResponse
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class MovieViewModel(app: Application, private val movieRepository: MovieRepository) :
    AndroidViewModel(app) {


    private val latestMoviesMutable: MutableLiveData<Resource<PopularMoviesResponse>> =
        MutableLiveData()
    val latestMovies: LiveData<Resource<PopularMoviesResponse>>
        get() = latestMoviesMutable
    private val popularMoviesMutable: MutableLiveData<Resource<PopularMoviesResponse>> =
        MutableLiveData()
    val popularMovies: LiveData<Resource<PopularMoviesResponse>>
        get() = popularMoviesMutable

    init {
        getPopularMovies()
        getLatestMovies()
    }

    private fun getPopularMovies() = viewModelScope.launch {

        try {
            popularMoviesMutable.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                val response = movieRepository.getPopularMovies()

                popularMoviesMutable.postValue(handleMoviesResponse(response))
            } else {
                popularMoviesMutable.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> popularMoviesMutable.postValue(Resource.Error("Network Failure"))
                else -> popularMoviesMutable.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun getLatestMovies() = viewModelScope.launch {

        try {
            latestMoviesMutable.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                val response = movieRepository.getLatestMovies()

                Log.e("viewmodel respon2e", response.toString())

                latestMoviesMutable.postValue(handleMoviesResponse(response))
            } else {
                latestMoviesMutable.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> latestMoviesMutable.postValue(Resource.Error("Network Failure"))
                else -> latestMoviesMutable.postValue(Resource.Error("Conversion Error"))
            }
        }


    }


    private fun handleMoviesResponse(response: Response<PopularMoviesResponse>): Resource<PopularMoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result: PopularMoviesResponse ->
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveArticle(movie: ResultDatabaseModel) = viewModelScope.launch {
        movieRepository.upsert(movie)
    }

    fun getSavedMovies() = movieRepository.getSavedMovie()


    fun deleteArticle(movie: ResultDatabaseModel) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }

    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<MovieApplicationClass>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}