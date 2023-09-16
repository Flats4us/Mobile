package com.example.flats4us21.services

import com.example.flats4us21.data.Meeting
import java.time.LocalDate
import java.time.Month

interface MeetingDataSource {
    fun getMeetings(date : LocalDate) : List<Meeting>
    fun getMeetingsOfMonth(month: Month, year : Int) : List<Meeting>
}