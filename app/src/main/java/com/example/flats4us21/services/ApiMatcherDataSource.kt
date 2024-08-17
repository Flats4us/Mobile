package com.example.flats4us21.services

import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiMatcherDataSource : MatcherDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: MatcherService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MatcherService::class.java)
    }

    override suspend fun getPotentialMatches(): ApiResult<List<StudentForMatcher>> {
        return try {
            val response = api.getPotentialMatches()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_potential_matches")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_potential_matches")
        }
    }

    override suspend fun getExistingMatches(): ApiResult<List<StudentForMatcher>> {
        return try {
            val response = api.getExistingMatches()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_existing_matches")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_existing_matches")
        }
    }

    override suspend fun acceptPotentialMatch(
        studentId: Int,
        decision: RentDecision
    ): ApiResult<String> {
        return try {
            val response = api.addNewMatch(studentId, decision)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_accepted_potential_match")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_accept_potential_match")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_accept_potential_match")
        }
    }
}