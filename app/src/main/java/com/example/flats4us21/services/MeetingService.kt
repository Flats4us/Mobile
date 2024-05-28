package com.example.flats4us21.services

import com.example.flats4us21.data.Meeting
import com.example.flats4us21.data.NewPropertyApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MeetingService {

    @GET("api/meetings")
    suspend fun getMeetings(): Response<List<Meeting>>

    @POST("api/meetings")
    suspend fun createMeeting(@Body meeting: Meeting): Response<NewPropertyApiResponse<String>>
}