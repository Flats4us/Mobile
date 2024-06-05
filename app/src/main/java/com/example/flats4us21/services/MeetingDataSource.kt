package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.dto.NewMeetingDto

interface MeetingDataSource {
    suspend fun getMeetings() : ApiResult<List<Meeting>>
    suspend fun createMeeting(meeting: NewMeetingDto) : ApiResult<String>
    suspend fun acceptMeeting(id: Int, decision: RentDecision): ApiResult<String>
}