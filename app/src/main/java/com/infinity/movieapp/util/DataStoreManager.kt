package com.infinity.movieapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UiMode {

    LIGHT,DARK
}
enum class IsFirst{
    FIRST, NO
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager (val context : Context){

    val uiModeFlow: Flow<UiMode> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->

            when (preference[IS_DARK_MODE] ?: false) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }
    val isFirstTimeFlow: Flow<IsFirst> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->

            when (preference[IS_FIRST_TIME] ?: false) {
                true -> IsFirst.FIRST
                false -> IsFirst.NO
            }
        }

    suspend fun setFirstTime(isFirst: IsFirst){
        context.dataStore.edit { preferences->
            preferences[IS_FIRST_TIME] = when(isFirst){
                IsFirst.FIRST -> true
                IsFirst.NO -> false
            }

        }
    }
    suspend fun setUiMode(uiMode: UiMode) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("dark_mode")
        val IS_FIRST_TIME = booleanPreferencesKey("first_time")
    }
}