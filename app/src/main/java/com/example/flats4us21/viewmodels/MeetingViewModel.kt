package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.dto.NewMeetingDto
import com.example.flats4us21.services.ApiMeetingDataSource
import com.example.flats4us21.services.MeetingDataSource
import kotlinx.coroutines.launch

private const val TAG = "MeetingViewModel"
class MeetingViewModel : ViewModel() {
    private val meetingRepository : MeetingDataSource = ApiMeetingDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    private val _meetings: MutableLiveData<List<Meeting>> = MutableLiveData()
    val meetings: MutableLiveData<List<Meeting>>
        get() = _meetings

    private var _meeting : NewMeetingDto? = null
    var meeting : NewMeetingDto?
        get() = _meeting
        set(value) {
            _meeting = value
        }

    fun getMeetings() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedMeetings = meetingRepository.getMeetings()) {
                    is ApiResult.Success -> {
                        val profileData = fetchedMeetings.data
                        _meetings.value = profileData
                        Log.i(TAG, profileData.toString())
                        Log.i(TAG, _meetings.value.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedMeetings.message}")
                        _errorMessage.value = fetchedMeetings.message
                        Log.e(TAG, "error: ${errorMessage.value}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createMeeting(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                Log.i(TAG, "meeting: $meeting")
                when(val fetchedMeetings = meetingRepository.createMeeting(meeting!!)) {
                    is ApiResult.Success -> {
                        val profileData = fetchedMeetings.data
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedMeetings.message}")
                        _errorMessage.value = fetchedMeetings.message
                        Log.e(TAG, "error: ${errorMessage.value}")
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
                callback(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptMeeting(id: Int, decision: Boolean, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                Log.i(TAG, "meeting: $meeting")
                when(val fetchedMeetings = meetingRepository.acceptMeeting(id, RentDecision(decision))) {
                    is ApiResult.Success -> {
                        val profileData = fetchedMeetings.data
                        _resultMessage.value = profileData
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedMeetings.message}")
                        _errorMessage.value = fetchedMeetings.message
                        Log.e(TAG, "error: ${errorMessage.value}")
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
                callback(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearResultMessage() {
        _resultMessage.value = null
    }
}