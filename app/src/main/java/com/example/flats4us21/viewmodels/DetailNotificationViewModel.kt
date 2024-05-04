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

private const val TAG = "DetailNotificationViewModel"
class DetailNotificationViewModel: ViewModel() {
    private val notificationRepository : NotificationDataSource = ApiNotificationDataSource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _notification = MutableLiveData<Notification>()
    val notification: LiveData<Notification>
        get() = _notification

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun getNotifications(notificationId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedNotification = notificationRepository.getNotification(notificationId)
                Log.d(TAG, "[getNotifications] Fetched notification: $fetchedNotification")
                _notification.value = fetchedNotification
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
}