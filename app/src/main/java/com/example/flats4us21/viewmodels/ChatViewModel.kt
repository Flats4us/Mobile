package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.services.ApiChatDataSource
import com.example.flats4us21.services.ChatDataSource
import kotlinx.coroutines.launch

private const val TAG = "ChatViewModel"

class ChatViewModel : ViewModel() {
    private val apiChatDataSource: ChatDataSource = ApiChatDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _userChats = MutableLiveData<List<Chat>>()
    val userChats: LiveData<List<Chat>> get() = _userChats

    private val _chatHistory = MutableLiveData<List<ChatMessage>>()
    val chatHistory: LiveData<List<ChatMessage>> get() = _chatHistory

    private val _chatParticipants = MutableLiveData<Int>()
    val chatParticipants: LiveData<Int> get() = _chatParticipants

    fun sendMessage(receiverUserId: Int, message: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val result = apiChatDataSource.sendMessage(receiverUserId, message)) {
                    is ApiResult.Success -> {
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${result.message}")
                        _errorMessage.postValue(result.message)
                        Log.e(TAG, "error: ${errorMessage.value}")
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                Log.e(TAG, "Exception $e")
                callback(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getChatHistory(chatId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                Log.d(TAG, "Requesting chat history for chatId: $chatId")
                when (val result = apiChatDataSource.getChatHistory(chatId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, "Received chat history for chatId: $chatId")
                        _chatHistory.postValue(result.data)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${result.message}")
                        _errorMessage.postValue(result.message)
                        Log.e(TAG, "error: ${errorMessage.value}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getChatParticipants(chatId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                Log.d(TAG, "Requesting chat participants for chatId: $chatId")
                when (val result = apiChatDataSource.getChatParticipants(chatId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, "Received chat participants for chatId: $chatId")
                        _chatParticipants.postValue(result.data)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${result.message}")
                        _errorMessage.postValue(result.message)
                        Log.e(TAG, "error: ${errorMessage.value}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getUserChats() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                Log.d(TAG, "Requesting user chats")
                when (val result = apiChatDataSource.getUserChats()) {
                    is ApiResult.Success -> {
                        Log.d(TAG, "Received user chats")
                        _userChats.postValue(result.data)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${result.message}")
                        _errorMessage.postValue(result.message)
                        Log.e(TAG, "error: ${errorMessage.value}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
