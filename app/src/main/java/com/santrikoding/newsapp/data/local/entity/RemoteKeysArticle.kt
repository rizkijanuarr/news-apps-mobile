package com.santrikoding.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_article")
data class RemoteKeysArticle(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
