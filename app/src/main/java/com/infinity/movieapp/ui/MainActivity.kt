package com.infinity.movieapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.infinity.movieapp.R
import com.infinity.movieapp.databinding.ActivityMainBinding
import com.infinity.movieapp.databse.MovieDatabase
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.DarkModeManager
import com.infinity.movieapp.util.UiMode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MovieViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var DarkModeManager: DarkModeManager
    var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = MovieRepository(MovieDatabase.invoke(this))
        val viewModelProviderFactory = MovieViewModelProvideFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        DarkModeManager = DarkModeManager(applicationContext)


           observeUiPreferences()
        binding.bottomNavigationView.setupWithNavController((navController))
    }

    private fun observeUiPreferences() {
        DarkModeManager.uiModeFlow.asLiveData().observe(this) { uiMode ->
            when (uiMode) {
                UiMode.LIGHT -> removeDarkMode()
                UiMode.DARK -> applyDarkMode()
                else -> removeDarkMode()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        lifecycleScope.launch {
            when (isDarkMode) {
                true -> {
                   DarkModeManager.setUiMode(UiMode.LIGHT)
                    removeDarkMode()

                }
                false -> {
                 DarkModeManager.setUiMode(UiMode.DARK)
                    applyDarkMode()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun removeDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
        isDarkMode = false
    }

    private fun applyDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
        isDarkMode = true
    }


}




