package com.santrikoding.newsapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.di.Injection
import com.santrikoding.newsapp.ui.home.HomeViewModel

class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository()
            )
        }
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}