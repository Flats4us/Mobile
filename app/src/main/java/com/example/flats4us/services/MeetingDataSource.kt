package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Meeting
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.dto.NewMeetingDto

interface MeetingDataSource {
    suspend fun getMeetings() : ApiResult<List<Meeting>>
    suspend fun createMeeting(meeting: NewMeetingDto) : ApiResult<String>
    suspend fun acceptMeeting(id: Int, decision: RentDecision): ApiResult<String>
}