package com.santrikoding.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: Repository
) : ViewModel() {

    fun searchArticle(search: String) = repository.searchSearchArticle(search).cachedIn(viewModelScope)

    fun setBookmark(article: ArticleEntity) {
        viewModelScope.launch {
            val isBookmark = article.isBookmark
            repository.setArticleBookmark(article, !isBookmark)
        }
    }
}