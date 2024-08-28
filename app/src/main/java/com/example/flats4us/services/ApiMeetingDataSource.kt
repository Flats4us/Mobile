package com.example.flats4us.services

import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Meeting
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.dto.NewMeetingDto
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiMeetingDataSource : MeetingDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: MeetingService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MeetingService::class.java)
    }

    override suspend fun getMeetings(): ApiResult<List<Meeting>> {
        return try {
            val response = api.getMeetings()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_meetings")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_meetings")
        }
    }

    override suspend fun createMeeting(meeting: NewMeetingDto): ApiResult<String> {
        return try {
            val response = api.createMeeting(meeting)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_created_meeting")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_create_meeting")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_create_meeting")
        }
    }

    override suspend fun acceptMeeting(id: Int, decision: RentDecision): ApiResult<String> {
        return try {
            val response = api.acceptMeeting(id, decision)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_sent_decision_about_meeting")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_accept_meeting")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_accept_meeting")
        }
    }
}