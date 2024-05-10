package com.santrikoding.newsapp.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.santrikoding.newsapp.data.local.NewsDatabase
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.data.local.entity.RemoteKeysArticle
import com.santrikoding.newsapp.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val apiService: ApiService,
    private val database: NewsDatabase
) : RemoteMediator<Int, ArticleEntity>() {

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleEntity>): RemoteKeysArticle? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysArticleDao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleEntity>): RemoteKeysArticle? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysArticleDao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleEntity>): RemoteKeysArticle? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysArticleDao().getRemoteKeysId(id.toString())
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getArticle(page = page)
            val endOfPaginationReached = responseData.data.data.isEmpty()
            val listArticleEntity = responseData.data.data.map { article ->
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

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysArticleDao().deleteRemoteKeys()
                    database.articleDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.data.data.map {
                    RemoteKeysArticle(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysArticleDao().insertAll(keys)
                database.articleDao().insertArticle(listArticleEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}