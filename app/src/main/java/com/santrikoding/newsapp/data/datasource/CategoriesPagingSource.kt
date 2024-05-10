package com.santrikoding.newsapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.santrikoding.newsapp.data.remote.ApiService
import com.santrikoding.newsapp.data.remote.model.Category
import retrofit2.HttpException
import java.io.IOException

class CategoriesPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Category>() {
    override fun getRefreshKey(state: PagingState<Int, Category>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Category> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getCategory(page = position)
            LoadResult.Page(
                data = response.data.data,
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