package com.santrikoding.newsapp.data.remote

import android.util.Log
import com.santrikoding.newsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val TAG = "ApiConfig"

    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.1.14:8001/api/")
            .baseUrl("https://news-api.appdev.my.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        Log.d(TAG, "Base URL: ${retrofit.baseUrl()}")

        return retrofit.create(ApiService::class.java)
    }
}
