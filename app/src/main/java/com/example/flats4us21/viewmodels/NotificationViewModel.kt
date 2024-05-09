package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.Notification
import com.example.flats4us21.services.ApiNotificationDataSource
import com.example.flats4us21.services.NotificationDataSource
import kotlinx.coroutines.launch

private const val TAG = "RealEstateViewModel"

class NotificationViewModel: ViewModel() {
    private val notificationRepository : NotificationDataSource = ApiNotificationDataSource

    private val _notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun getUserNotifications(){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedProperties = notificationRepository.getNotification()
                Log.d(TAG, "[getUserProperties] Fetched properties: $fetchedProperties")
                _notifications.value = fetchedProperties
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private var _selectedNotification: Notification? = null
    var selectedNotification: Notification?
        get() = _selectedNotification
        set(value) {
            _selectedNotification = value
        }
}