package com.santrikoding.newsapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.withDateFormat(): String {
    val expectFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault()).parse(this) as Date
    return expectFormat.format(oldFormat)
}