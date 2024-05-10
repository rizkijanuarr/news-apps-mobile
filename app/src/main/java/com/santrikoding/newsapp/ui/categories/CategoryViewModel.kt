package com.santrikoding.newsapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.data.remote.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryViewModel(private val repository: Repository) : ViewModel() {
    val getCategory: Flow<PagingData<Category>> =
        repository.getCategory().cachedIn(viewModelScope)
}