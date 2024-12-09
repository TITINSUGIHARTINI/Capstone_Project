package com.dicoding.matchsense.data.remote.response

import com.google.gson.annotations.SerializedName

data class ThesaurusResponse(

	@field:SerializedName("synonyms")
	val synonyms: List<String>? = null,

	@field:SerializedName("antonyms")
	val antonyms: List<String>? = null,

	@field:SerializedName("word")
	val word: String? = null
)
