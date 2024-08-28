package com.example.flats4us.services

import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Argument
import com.example.flats4us.data.dto.NewArgumentDto
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiArgumentDataSource"
class ApiArgumentDataSource: ArgumentDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: ArgumentService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ArgumentService::class.java)
    }

    override suspend fun getArguments(): ApiResult<List<Argument>> {
        return try {
            val response = api.getArguments()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_fail_to_retrieve_arguments")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_fail_to_retrieve_arguments")
        }
    }

    override suspend fun addArgument(argument: NewArgumentDto): ApiResult<String> {
        return try {
            val response = api.addArgument(argument)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_added_argument")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_fail_to_add_argument")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_fail_to_add_argument")
        }
    }

    override suspend fun ownerAcceptArgument(id: Int): ApiResult<String> {
        return try {
            val response = api.ownerAcceptArgument(id)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_accepted_argument_from_student_side")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_accept_argument")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_accept_argument")
        }
    }

    override suspend fun studentAcceptArgument(argumentId: Int): ApiResult<String> {
        return try {
            val response = api.studentAcceptArgument(argumentId)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_accepted_argument_from_owner_side")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_accept_argument")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_accept_argument")
        }
    }

    override suspend fun askingForIntervention(id: Int): ApiResult<String> {
        return try {
            val response = api.askingForIntervention(id)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_asked_moderator_for_intervention")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_asking_for_intervention")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_asking_for_intervention")
        }
    }
}