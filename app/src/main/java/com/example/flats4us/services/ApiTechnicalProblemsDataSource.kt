package com.example.flats4us.services

import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.NewTechnicalProblemsDto
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTechnicalProblemsDataSource : TechnicalProblemsDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: TechnicalProblemService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TechnicalProblemService::class.java)
    }

    override suspend fun addTechnicalProblems(newTechnicalProblemsDto: NewTechnicalProblemsDto): ApiResult<String> {
        return try {
            val response = api.addTechnicalProblems(newTechnicalProblemsDto)
            if(response.isSuccessful) {
                val data = response.body()
                ApiResult.Success("success_reported_technical_problem")
            } else {
                ApiResult.Error("error_failed_to_add_technical_problem")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_technical_problem")
        }
    }
}