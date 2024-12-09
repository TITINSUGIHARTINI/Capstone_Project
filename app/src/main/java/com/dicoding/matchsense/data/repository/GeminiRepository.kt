package com.dicoding.matchsense.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.utils.Key
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig

class GeminiRepository {

    fun getResponse(request: String): LiveData<Result<String?>> = liveData {
        emit(Result.Loading)
        try {
            val model = model()
            val response = model.generateContent(request)
            emit(Result.Success(response.text))
        } catch (e: Exception) {
            Log.e("Gemini", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    private fun model(): GenerativeModel {

        val apiKey = Key.GEMINI_API_KEY

        val generationConfig = GenerationConfig.Builder().apply {
            temperature = 0.9f
            topK = 16
            topP = 0.1f
        }.build()

        val generativeModel = GenerativeModel(
            "gemini-1.5-flash",
            apiKey,
            generationConfig
        )

        return generativeModel
    }

    companion object {
        @Volatile
        private var instance: GeminiRepository? = null
        fun getInstance(): GeminiRepository =
            instance ?: synchronized(this) {
                instance ?: GeminiRepository()
            }.also { instance = it }
    }

}