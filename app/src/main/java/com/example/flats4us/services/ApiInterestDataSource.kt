package com.example.flats4us.services

import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Interest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInterestDataSource : InterestDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: InterestService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(InterestService::class.java)
    }

    override suspend fun getInterests(): ApiResult<List<Interest>> {
        return try {
            val response = api.getInterests()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_interests")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_interests")
        }
    }

}
