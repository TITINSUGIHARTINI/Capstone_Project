package com.dicoding.matchsense.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.model.authentication.LoginRequest
import com.dicoding.matchsense.data.model.authentication.RegisterRequest
import com.dicoding.matchsense.data.pref.UserModel
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.remote.response.ErrorResponse
import com.dicoding.matchsense.data.remote.response.LoginMSResponse
import com.dicoding.matchsense.data.remote.response.LoginResponse
import com.dicoding.matchsense.data.remote.response.RegisterMSResponse
import com.dicoding.matchsense.data.remote.retrofit.service.ApiService
import com.dicoding.matchsense.data.remote.retrofit.service.ApiServiceDicoding
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getUserPreference(): UserPreference {
        return userPreference
    }

    fun signUp(name: String, email: String, password: String): LiveData<Result<RegisterMSResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val request = RegisterRequest(name,email,password)
                val response = apiService.register(request)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                Log.e("Signup", "HTTP Error: ${e.message}")
                try {
                    val errorResponse = e.response()?.errorBody()?.string()
                    val parsedError = Gson().fromJson(errorResponse, RegisterMSResponse::class.java)
                    emit(Result.Success(parsedError))
                } catch (parseException: Exception) {
                    Log.e("Signuo", "Parsing Error: ${parseException.message}")
                    emit(Result.Error("Error: ${e.message}"))
                }
            } catch (e: Exception) {
                Log.e("Signup", "Error: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginMSResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e("Login", "HTTP Error: ${e.message}")
            try {
                val errorResponse = e.response() ?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorResponse, LoginMSResponse::class.java)
                emit(Result.Success(parsedError))
            } catch (parseException: Exception) {
                Log.e("Login", "Parsing Error: ${parseException.message}")
                emit(Result.Error("Error: ${e.message}"))
            }
        } catch (e: Exception) {
            Log.e("Login", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun signUp(name: String, email: String, password: String): LiveData<Result<ErrorResponse>> =
//        liveData {
//            emit(Result.Loading)
//            try {
//                val response = apiService.register(name, email, password)
//                emit(Result.Success(response))
//            } catch (e: HttpException) {
//                Log.e("Signup", "HTTP Error: ${e.message}")
//                try {
//                    val errorResponse = e.response()?.errorBody()?.string()
//                    val parsedError = Gson().fromJson(errorResponse, ErrorResponse::class.java)
//                    emit(Result.Success(parsedError))
//                } catch (parseException: Exception) {
//                    Log.e("Signuo", "Parsing Error: ${parseException.message}")
//                    emit(Result.Error("Error: ${e.message}"))
//                }
//            } catch (e: Exception) {
//                Log.e("Signup", "Error: ${e.message}")
//                emit(Result.Error(e.message.toString()))
//            }
//        }
//
//    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.login(email, password)
//            emit(Result.Success(response))
//        } catch (e: HttpException) {
//            Log.e("Login", "HTTP Error: ${e.message}")
//            try {
//                val errorResponse = e.response()?.errorBody()?.string()
//                val parsedError = Gson().fromJson(errorResponse, LoginResponse::class.java)
//                emit(Result.Success(parsedError))
//            } catch (parseException: Exception) {
//                Log.e("Login", "Parsing Error: ${parseException.message}")
//                emit(Result.Error("Error: ${e.message}"))
//            }
//        } catch (e: Exception) {
//            Log.e("Login", "Error: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}