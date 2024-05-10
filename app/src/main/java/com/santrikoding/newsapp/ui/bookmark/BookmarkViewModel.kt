package com.santrikoding.newsapp.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: Repository) : ViewModel() {

    fun getBookmarkArticle() = repository.getBookmarkArticle()

    fun setBookmark(articleEntity: ArticleEntity) {
        viewModelScope.launch {
            val isBookmarked = articleEntity.isBookmark
            repository.setArticleBookmark(articleEntity, !isBookmarked)
        }
    }
}