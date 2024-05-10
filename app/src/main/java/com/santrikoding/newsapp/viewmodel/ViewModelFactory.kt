package com.santrikoding.newsapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.di.Injection
import com.santrikoding.newsapp.ui.bookmark.BookmarkViewModel
import com.santrikoding.newsapp.ui.categories.CategoryViewModel
import com.santrikoding.newsapp.ui.detail.DetailViewModel
import com.santrikoding.newsapp.ui.home.HomeViewModel
import com.santrikoding.newsapp.ui.search.SearchViewModel
import com.santrikoding.newsapp.utils.SettingPreferences

class ViewModelFactory(
    private val repository: Repository,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository(context),
                Injection.providePrefDataStore(context)
            )
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository, settingPreferences) as T
            }
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                CategoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}