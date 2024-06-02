package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Meeting
import java.time.LocalDate
import java.time.Month

interface MeetingDataSource {
    suspend fun getMeetings() : ApiResult<List<Meeting>>
    suspend fun createMeeting(meeting: Meeting) : ApiResult<String>
}