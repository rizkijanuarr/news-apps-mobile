package com.santrikoding.newsapp.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME_KEY = booleanPreferencesKey("theme_settings")

    fun getThemeSettings(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false // Provide a default value if THEME_KEY is null
        }
    }

    suspend fun saveThemeSettings(isDarkModeActive: Boolean) {
        dataStore.edit { preferences -> preferences[THEME_KEY] = isDarkModeActive }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingPreferences(dataStore).also { INSTANCE = it }
            }
        }
    }
}
