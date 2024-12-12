package com.dicoding.matchsense.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginMSResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("msg")
	val msg: String? = null
)
