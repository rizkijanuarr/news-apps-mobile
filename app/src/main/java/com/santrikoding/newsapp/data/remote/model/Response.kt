package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Response<T>(

	@field:SerializedName("data")
	val data: T,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)