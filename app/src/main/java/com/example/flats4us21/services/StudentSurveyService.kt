package com.example.flats4us21.services

import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(IStudentSurveyService::class.java)

    suspend fun getSurveyQuestion(surveyType: String): ApiResult<List<SurveyQuestion>> {
        return try {
            val response = if (surveyType == "Student"){
                api.getStudentSurveyQuestions()
            } else {
                api.getOwnerSurveyQuestions()
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