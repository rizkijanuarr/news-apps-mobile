package com.santrikoding.newsapp.di

import com.santrikoding.newsapp.data.remote.ApiConfig
import com.santrikoding.newsapp.data.Repository

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}