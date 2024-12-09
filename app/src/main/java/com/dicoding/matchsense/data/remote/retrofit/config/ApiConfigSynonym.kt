package com.dicoding.matchsense.data.remote.retrofit.config

import com.dicoding.matchsense.BuildConfig
import com.dicoding.matchsense.data.remote.retrofit.service.ApiService
import com.dicoding.matchsense.data.remote.retrofit.service.ApiServiceSynonym
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfigSynonym {
    fun getApiService(): ApiServiceSynonym {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requesHeaders = req.newBuilder()
            chain.proceed(requesHeaders.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SYNONYM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiServiceSynonym::class.java)
    }
}