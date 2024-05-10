package com.santrikoding.newsapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.santrikoding.newsapp.data.local.NewsDatabase
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

class ArticlePagingSource(
    private val apiService: ApiService,
    private val database: NewsDatabase,
    private val search: String
) : PagingSource<Int, ArticleEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getArticle(page = position, search = search)
            val listArticle = response.data.data.map { article ->
                val isBookmarked = database.articleDao().isArticleBookmarked(article.id)
                ArticleEntity(
                    id = article.id,
                    image = article.image,
                    title = article.title,
                    content = article.content,
                    category = article.category.name,
                    author = article.user.name,
                    date = article.createdAt,
                    isBookmark = isBookmarked,
                    slug = article.slug
                )
            }
            LoadResult.Page(
                data = listArticle,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.data.data.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }
}