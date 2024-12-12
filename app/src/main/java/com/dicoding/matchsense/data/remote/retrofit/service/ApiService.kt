package com.dicoding.matchsense.data.remote.retrofit.service

import com.dicoding.matchsense.data.model.LoginRequest
import com.dicoding.matchsense.data.model.RegisterRequest
import com.dicoding.matchsense.data.remote.response.LoginMSResponse
import com.dicoding.matchsense.data.remote.response.RegisterMSResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun register(
       @Body registerRequest: RegisterRequest
    ): RegisterMSResponse

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginMSResponse

//    @FormUrlEncoded
//    @POST("login")
//    suspend fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): LoginMSResponse
//
//    @FormUrlEncoded
//    @POST("register")
//    suspend fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): RegisterMSResponse

}