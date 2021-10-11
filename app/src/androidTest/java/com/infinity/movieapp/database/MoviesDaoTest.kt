package com.infinity.movieapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinity.movieapp.getOrAwaitValue
import com.infinity.movieapp.models.databasemodels.ResultDatabaseModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MoviesDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesDao: MoviesDao

    private lateinit var movieDatabase: MovieDatabase

    @Before
    fun start() {
        movieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        moviesDao = movieDatabase.getMovieDAO()
    }


    @Test
    fun insertMoviesItem() = runBlockingTest {
        val movieItem = ResultDatabaseModel(
            id = "1",
            backdrop_path = "some_path",
            original_title = "civil war",
            overview = "an avengers movie",
            popularity = "4.2",
            poster_path = "poster_path",
            release_date = "date",
            title = "Civil War",
            vote_average = "4",
            popular = true,
            latest = false

        )
        moviesDao.upsert(listOf(movieItem))
        val result = moviesDao.getAllPopularMovies().getOrAwaitValue()
        assertThat(result.contains(movieItem)).isTrue()
    }

    @Test
    fun replaceMoviesItem() = runBlockingTest {
        val movieItem = listOf(ResultDatabaseModel(
            id = "1",
            backdrop_path = "some_path",
            original_title = "civil war",
            overview = "an avengers movie",
            popularity = "4.2",
            poster_path = "poster_path",
            release_date = "date",
            title = "Civil War",
            vote_average = "4",
            popular = true,
            latest = false

        ),
            ResultDatabaseModel(
                id = "4",
                backdrop_path = "some_path",
                original_title = "civil war",
                overview = "an avengers movie",
                popularity = "4.2",
                poster_path = "poster_path",
                release_date = "date",
                title = "Civil War",
                vote_average = "4",
                popular = true,
                latest = false

            ),
            ResultDatabaseModel(
                id = "7",
                backdrop_path = "some_path",
                original_title = "civil war",
                overview = "an avengers movie",
                popularity = "4.2",
                poster_path = "poster_path",
                release_date = "date",
                title = "Civil War",
                vote_average = "4",
                popular = true,
                latest = false

            ))
        val result = moviesDao.getAllPopularMovies().getOrAwaitValue()
        assertThat(result.contains(movieItem[1]))
        val movieItem2 = ResultDatabaseModel(
            id = "1",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        )
        moviesDao.upsert(listOf(movieItem2))
        val result2 = moviesDao.getAllPopularMovies().getOrAwaitValue()

        assertThat(result2.contains(movieItem[0])).isFalse()
    }

    @Test
    fun getAllPopularMoviesTest() = runBlockingTest {
        val popularMovies = listOf<ResultDatabaseModel>(ResultDatabaseModel(
            id = "1",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ), ResultDatabaseModel(
            id = "2",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = true,
            latest = false

        ), ResultDatabaseModel(
            id = "3",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = true,
            latest = false

        ), ResultDatabaseModel(
            id = "4",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = true,
            latest = false

        ))
        moviesDao.upsert(popularMovies)

        val result = moviesDao.getAllPopularMovies().getOrAwaitValue { }


        assertThat(result.contains(popularMovies[0])).isFalse()

    }

    @Test
    fun getAllLatestMoviesTest() = runBlockingTest {
        val popularMovies = listOf<ResultDatabaseModel>(ResultDatabaseModel(
            id = "1",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = true,
            latest = false

        ), ResultDatabaseModel(
            id = "2",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ), ResultDatabaseModel(
            id = "3",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ), ResultDatabaseModel(
            id = "4",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ))
        moviesDao.upsert(popularMovies)

        val result = moviesDao.getAlllatestMovies().getOrAwaitValue { }


        assertThat(result.contains(popularMovies[0])).isFalse()

    }
    @Test
    fun deleteMovieTest() = runBlockingTest {
        val popularMovies = listOf<ResultDatabaseModel>(ResultDatabaseModel(
            id = "1",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = true,
            latest = false

        ), ResultDatabaseModel(
            id = "2",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ), ResultDatabaseModel(
            id = "3",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ), ResultDatabaseModel(
            id = "4",
            backdrop_path = "somse_path",
            original_title = "csivil war",
            overview = "an avesngers movie",
            popularity = "4.2s",
            poster_path = "posdter_path",
            release_date = "dsate",
            title = "Civil Wsar",
            vote_average = "5d",
            popular = false,
            latest = true

        ))
        moviesDao.upsert(popularMovies)

        moviesDao.deleteMovie(popularMovies[2])

        val result = moviesDao.getAlllatestMovies().getOrAwaitValue { }


        assertThat(result.contains(popularMovies[2])).isFalse()

    }

}