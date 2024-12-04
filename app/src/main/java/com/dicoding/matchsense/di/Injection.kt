package com.dicoding.matchsense.di

import android.content.Context
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.data.remote.ApiConfig
import com.dicoding.matchsense.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, user)
    }
}