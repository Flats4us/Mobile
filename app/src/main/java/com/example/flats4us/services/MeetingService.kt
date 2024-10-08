package com.example.flats4us.services

import com.example.flats4us.data.Meeting
import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.dto.NewMeetingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MeetingService {

    @GET("api/meetings")
    suspend fun getMeetings(): Response<List<Meeting>>

    @POST("api/meetings")
    suspend fun createMeeting(@Body meeting: NewMeetingDto): Response<NewPropertyApiResponse<String>>

    @PUT("api/meetings/{id}/accept")
    suspend fun acceptMeeting(@Path("id") id: Int, @Body decision: RentDecision): Response<NewPropertyApiResponse<String>>
}