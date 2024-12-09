package com.dicoding.matchsense.data.remote.retrofit.service

import com.dicoding.matchsense.data.remote.response.ThesaurusResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceSynonym {
    @GET("thesaurus")
    suspend fun getSynonyms(
        @Query("word") word: String
    ): ThesaurusResponse
}