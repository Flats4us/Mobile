package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Notification
import com.example.flats4us21.services.ApiNotificationDataSource
import kotlinx.coroutines.launch

private const val TAG = "NotificationViewModel"

class NotificationViewModel : ViewModel() {
    private val apiNotificationRepository = ApiNotificationDataSource()

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
        apiNotificationRepository.setOnReceiveNotificationCallback { title, body ->
            Log.d(TAG, "Otrzymano powiadomienie  $title: $body")
            val currentNotifications = _notifications.value?.toMutableList() ?: mutableListOf()
            val newNotification = Notification(
                notificationId = _notifications.value!!.size + 1,
                title = title,
                body = body,
                dateTime = System.currentTimeMillis().toString(),
                read = true,
            )
            currentNotifications.add(newNotification)
            _notifications.postValue(currentNotifications)
            Log.d(TAG, "Zaktualizowano powiadomienia: $currentNotifications")
        }
    }

    fun getUnreadNotifications() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedNotifications = apiNotificationRepository.getUnreadNotifications()) {
                    is ApiResult.Success -> {
                        val notificationData = fetchedNotifications.data
                        _notifications.postValue(notificationData)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedNotifications.message}")
                        _errorMessage.value = fetchedNotifications.message
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

    fun getAllNotifications() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedNotifications = apiNotificationRepository.getAllNotifications()) {
                    is ApiResult.Success -> {
                        val notificationData = fetchedNotifications.data
                        _notifications.postValue(notificationData)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedNotifications.message}")
                        _errorMessage.value = fetchedNotifications.message
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

    fun getAllNotifications(notificationIds: List<Int>) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedNotifications = apiNotificationRepository.markNotificationsAsRead(notificationIds)) {
                    is ApiResult.Success -> {
                        val notificationData = fetchedNotifications.data
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedNotifications.message}")
                        _errorMessage.value = fetchedNotifications.message
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

    fun startConnection() {
        apiNotificationRepository.startConnection()
        Log.d(TAG, "Rozpoczęto połączenie")
    }

    fun stopConnection() {
        apiNotificationRepository.stopConnection()
        Log.d(TAG, "Zakończono połączenie")
    }
}
