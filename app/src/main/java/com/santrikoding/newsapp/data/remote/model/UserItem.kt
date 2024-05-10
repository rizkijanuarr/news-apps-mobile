package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("email_verified_at")
    val emailVerifiedAt: Any,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String
)