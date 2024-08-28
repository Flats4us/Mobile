package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.SurveyQuestion
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

object StudentSurveyService {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val okHttpClientWithoutAuth = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(IStudentSurveyService::class.java)

    private val apiWithoutAuth = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClientWithoutAuth)
        .build()
        .create(IStudentSurveyService::class.java)

    suspend fun getSurveyQuestion(surveyType: String): ApiResult<List<SurveyQuestion>> {
        return try {
            Log.d("SurveyService", "surveyType: $surveyType")
            val response = if (surveyType.uppercase(Locale.getDefault()) == "OWNER"){
                api.getOwnerSurveyQuestions()
            } else {
                apiWithoutAuth.getStudentSurveyQuestions()
            }
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        }  catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }
}