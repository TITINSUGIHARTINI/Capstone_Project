package com.dicoding.matchsense.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.remote.response.ThesaurusResponse
import com.dicoding.matchsense.data.remote.retrofit.service.ApiService
import com.dicoding.matchsense.data.remote.retrofit.service.ApiServiceSynonym

class SynonymRepository private constructor(
    private val apiService: ApiServiceSynonym
) {

    suspend fun getSynonym(word: String): LiveData<Result<ThesaurusResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSynonyms(word)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("Signup", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: SynonymRepository? = null
        fun getInstance(
            apiService: ApiServiceSynonym
        ): SynonymRepository =
            instance ?: synchronized(this) {
                instance ?: SynonymRepository(apiService)
            }.also { instance = it }
    }

}