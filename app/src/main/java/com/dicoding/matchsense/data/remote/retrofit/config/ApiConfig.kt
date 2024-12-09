package com.dicoding.matchsense.data.remote.retrofit.config

import com.dicoding.matchsense.data.pref.UserPreference
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.dicoding.matchsense.BuildConfig
import com.dicoding.matchsense.data.remote.retrofit.service.ApiService


object ApiConfig {
    fun getApiService(userPreference: UserPreference): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val token = runBlocking { userPreference.getSession().firstOrNull()?.token }
            val requesHeaders = req.newBuilder()
            if (!token.isNullOrEmpty()) {
                requesHeaders.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requesHeaders.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}