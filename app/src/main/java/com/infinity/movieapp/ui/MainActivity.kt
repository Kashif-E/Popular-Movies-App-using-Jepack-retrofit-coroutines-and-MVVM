package com.infinity.movieapp.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.infinity.movieapp.R
import com.infinity.movieapp.database.MovieDatabase
import com.infinity.movieapp.databinding.ActivityMainBinding
import com.infinity.movieapp.extensions.toast
import com.infinity.movieapp.repository.MovieRepository
import com.infinity.movieapp.util.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MovieViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var repository: MovieRepository
    private lateinit var dataStoreManager: DataStoreManager
    var isDarkMode = false


    override fun onCreate(savedInstanceState: Bundle?) {

        dataStoreManager = DataStoreManager(applicationContext)
        observeUiPreferences()
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        repository = MovieRepository(MovieDatabase.invoke(this))

        val viewModelProviderFactory = MovieViewModelProvideFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController((navController))

        viewModel.state.observe(this, {
            when (it) {
                is MyState.Fetched -> {
                    this.toast("Internet Available")
                }
                is MyState.Error -> {
                    this.toast("Internet Unavailable")
                }
            }
        })
    }


    private fun observeUiPreferences() {
        dataStoreManager.uiModeFlow.asLiveData().observe(this) { uiMode ->
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

        if (item.itemId == R.id.app_bar_switch) {
            lifecycleScope.launch {
                when (isDarkMode) {
                    true -> {
                        dataStoreManager.setUiMode(UiMode.LIGHT)
                        removeDarkMode()

                    }
                    false -> {
                        dataStoreManager.setUiMode(UiMode.DARK)
                        applyDarkMode()
                    }
                }
            }
            return true
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp())
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




