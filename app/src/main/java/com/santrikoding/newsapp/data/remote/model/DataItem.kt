package com.santrikoding.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Sliders(

	@field:SerializedName("image")
	val image: String = "",

	@field:SerializedName("updated_at")
	val updatedAt: String = "",

	@field:SerializedName("link")
	val link: String = "",

	@field:SerializedName("created_at")
	val createdAt: String = "",

	@field:SerializedName("id")
	val id: Int = 0
)