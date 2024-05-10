package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Article(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("category_id")
    val categoryId: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("views_count")
    val viewsCount: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("slug")
    val slug: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("category")
    val category: Category
)