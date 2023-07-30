package com.example.flats4us21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.services.HardcodedMeetingDataSource
import com.example.flats4us21.services.MeetingDataSource
import java.time.LocalDate
import java.time.Month

class MeetingViewModel : ViewModel() {
    private val meetingRepository : MeetingDataSource = HardcodedMeetingDataSource()

    fun getMeetings(date: LocalDate): LiveData<List<Meeting>> {
        val meetings = meetingRepository.getMeetings(date)
        return MutableLiveData(meetings)
    }

    fun getMeetingsOfMonth(month: Month, year: Int): LiveData<List<Meeting>> {
        val meetings = meetingRepository.getMeetingsOfMonth(month, year)
        return MutableLiveData(meetings)
    }
}