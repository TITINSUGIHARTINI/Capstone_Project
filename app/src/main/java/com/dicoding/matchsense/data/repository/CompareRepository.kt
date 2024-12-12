package com.dicoding.matchsense.data.repository


import com.dicoding.matchsense.data.remote.response.CompareResponse
import com.dicoding.matchsense.data.remote.retrofit.service.ApiServiceCompare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompareRepository (private val apiServiceCompare: ApiServiceCompare) {

    suspend fun getSimilarity(sentence1: String, sentence2: String): CompareResponse {
        return withContext(Dispatchers.IO) {
            val response = apiServiceCompare.getSimilarity(sentence1, sentence2)
            response
        }
    }

    companion object {
        @Volatile
        private var instance: CompareRepository? = null
        fun getInstance(
            apiService: ApiServiceCompare
        ): CompareRepository =
            instance ?: synchronized(this) {
                instance ?: CompareRepository(apiService)
            }.also { instance = it }
    }
}