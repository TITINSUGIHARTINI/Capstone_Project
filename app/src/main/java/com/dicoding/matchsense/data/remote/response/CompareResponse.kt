package com.dicoding.matchsense.data.remote.response

import com.google.gson.annotations.SerializedName

data class CompareResponse(

	@field:SerializedName("similarity_score")
	val similarityScore: Float? = null,

	@field:SerializedName("predicted_label")
	val predictedLabel: Int? = null
)
