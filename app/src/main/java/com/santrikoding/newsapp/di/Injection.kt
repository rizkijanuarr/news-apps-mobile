package com.santrikoding.newsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.data.local.NewsDatabase
import com.santrikoding.newsapp.data.remote.ApiConfig
import com.santrikoding.newsapp.utils.SettingPreferences

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getDatabase(context)
        return Repository.getInstance(apiService, database)
    }

    // Use the correct import for Preferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun providePrefDataStore(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(dataStore = context.dataStore)
    }
}
