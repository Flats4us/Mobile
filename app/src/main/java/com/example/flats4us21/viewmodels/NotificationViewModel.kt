package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.Notification
import com.example.flats4us21.services.SignalRService
import kotlinx.coroutines.launch

private const val TAG = "NotificationViewModel"

class NotificationViewModel : ViewModel() {

    private val signalRService = SignalRService()
    private val _notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init {
        startSignalRConnection()
        observeSignalRNotifications()
    }

    private fun startSignalRConnection() {
        viewModelScope.launch {
            try {
                signalRService.startConnection()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            }
        }
    }

    private fun observeSignalRNotifications() {
        signalRService.notifications.observeForever { notification ->
            notification?.let {
                val currentList = _notifications.value.orEmpty().toMutableList()
                currentList.add(it)
                _notifications.value = currentList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        signalRService.stopConnection()
    }
}
