package com.dicoding.matchsense.data.remote.retrofit.service

import com.dicoding.matchsense.data.remote.response.CompareResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServiceCompare {

    @FormUrlEncoded
    @POST("/predict")
    suspend fun getSimilarity(
        @Field("sentence1") sentence1: String,
        @Field("sentence2") sentence2: String
    ): CompareResponse

}