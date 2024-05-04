package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.serializer.UserSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder

private const val TAG = "ApiUserDataSource"
object ApiUserDataSource: UserDataSource{


    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(NewUserDto::class.java, UserSerializer())
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)
    }

    override suspend fun login(email: String, password: String): ApiResult<LoginResponse?> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = api.login(loginRequest)
            if(response.isSuccessful) {
                val data = response.body()
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    override suspend fun register(user: NewUserDto) {
        api.registerUser(user)
    }

    override suspend fun checkEmail(email: String): ApiResult<Boolean> {
        return try {
            val encodedEmail = URLEncoder.encode(email, "UTF-8")
            Log.i(TAG, "Encoded email: $encodedEmail")
            val response = api.checkEmail(encodedEmail)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }
}