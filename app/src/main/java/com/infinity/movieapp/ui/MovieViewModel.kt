package com.infinity.movieapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinity.movieapp.MovieApplicationClass
import com.infinity.movieapp.models.PopularMoviesModel
import com.infinity.movieapp.models.Result
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.appcompat.app.AppCompatDelegate
import java.io.IOException


class MovieViewModel (app: Application, val movieRepository : MovieRepository) : AndroidViewModel(app) {


    val popularMovies : MutableLiveData <Resource<PopularMoviesModel>> = MutableLiveData()
    val latestMovies : MutableLiveData <Resource<PopularMoviesModel>> = MutableLiveData()
    init {
        getPopularMovies()
        getLatestMovies()
    }
    fun getPopularMovies() = viewModelScope.launch {

        try {
            popularMovies.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                val response = movieRepository.getPopularMovies()

                popularMovies.postValue(handleMoviesREsponse(response))
            } else {
                popularMovies.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> popularMovies.postValue(Resource.Error("Network Failure"))
                else -> popularMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


        fun getLatestMovies() = viewModelScope.launch {

            try {
                latestMovies.postValue(Resource.Loading())
                if (hasInternetConnection()) {
                    val response = movieRepository.getLatestMovies()

                    latestMovies.postValue(handleMoviesREsponse(response))
                } else {
                    latestMovies.postValue(Resource.Error("No Internet Connection"))
                }
            }catch(t: Throwable) {
                when(t) {
                    is IOException -> latestMovies.postValue(Resource.Error("Network Failure"))
                    else -> latestMovies.postValue(Resource.Error("Conversion Error"))
                }
            }


        }


    private fun  handleMoviesREsponse(response: Response<PopularMoviesModel>) : Resource<PopularMoviesModel>
    {
        if (response.isSuccessful)
        {
            response.body()?.let { result: PopularMoviesModel ->
              return  Resource.Success(result)
            }
        }

        return  Resource.Error(response.message())
    }

    fun saveArticle(movie:Result) = viewModelScope.launch {
        movieRepository.upsert(movie)
    }

    fun getSavedNews() = movieRepository.getSavedMovie()


    fun deleteArticle( movie: Result) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }

    private fun hasInternetConnection() : Boolean
    {

        val  connectivityManager = getApplication<MovieApplicationClass>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val network = connectivityManager.activeNetwork ?: return  false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return  false
            return  when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else
        {
            connectivityManager.activeNetworkInfo?.run {
                return  when(type) {
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