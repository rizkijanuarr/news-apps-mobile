package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Category(

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("updated_at")
    val updatedAt: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("created_at")
    val createdAt: String = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("slug")
    val slug: String = "",

    @field:SerializedName("posts")
    val post: List<Article> = emptyList()
)