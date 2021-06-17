package com.infinity.movieapp.util

import android.content.Context

import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
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
class DataStoreManager (context : Context){

    private val dataStore = context.createDataStore("settings")

    val uiModeFlow: Flow<UiMode> = dataStore.data
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
    val isFirstTimeFlow: Flow<IsFirst> = dataStore.data
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
        dataStore.edit { preferences->
            preferences[IS_FIRST_TIME] = when(isFirst){
                IsFirst.FIRST -> true
                IsFirst.NO -> false
            }

        }
    }
    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
        val IS_FIRST_TIME = preferencesKey<Boolean>("first_time")
    }
}