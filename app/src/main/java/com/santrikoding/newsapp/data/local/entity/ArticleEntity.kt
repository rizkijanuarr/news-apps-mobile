package com.santrikoding.newsapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tbl_article")
@Parcelize
data class ArticleEntity(
    @PrimaryKey
    var id: Int = 0,
    var image: String = "",
    var title: String = "",
    var content: String = "",
    var category: String = "",
    var author: String = "",
    var date: String = "",
    var slug: String = "",
    var isBookmark: Boolean
) : Parcelable
