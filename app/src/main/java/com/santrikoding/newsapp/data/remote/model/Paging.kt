package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Paging<T>(

    @field:SerializedName("data")
    val data: List<T>

)