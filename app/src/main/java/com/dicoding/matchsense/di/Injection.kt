package com.dicoding.matchsense.di

import android.content.Context
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.data.remote.retrofit.config.ApiConfig
import com.dicoding.matchsense.data.remote.retrofit.config.ApiConfigSynonym
import com.dicoding.matchsense.data.repository.SynonymRepository
import com.dicoding.matchsense.data.repository.UserRepository
import com.dicoding.matchsense.data.repository.GeminiRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, user)
    }

    fun synonymRepository(): SynonymRepository {
        val apiService = ApiConfigSynonym.getApiService()
        return SynonymRepository.getInstance(apiService)
    }

    fun geminiRepository(): GeminiRepository {
        return GeminiRepository.getInstance()
    }
}