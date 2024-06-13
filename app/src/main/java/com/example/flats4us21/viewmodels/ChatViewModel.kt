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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

private const val TAG = "ChatViewModel"

class ChatViewModel : ViewModel() {
    private val apiChatDataSource: ApiChatDataSource = ApiChatDataSource()

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

    init {
        apiChatDataSource.setOnReceivePrivateMessageCallback { userId, message, date ->
            Log.d(TAG, "Message received from $userId: $message")
            val currentChatHistory = _chatHistory.value?.toMutableList() ?: mutableListOf()
            val newMessage = ChatMessage(
                chatMessageId = currentChatHistory.size + 1,
                content = message,
                dateTime = date,
                senderId = userId
            )
            currentChatHistory.add(newMessage)
            _chatHistory.postValue(currentChatHistory)
            Log.d(TAG, "Updated chat history: $currentChatHistory")
        }
    }

    fun startConnection() {
        apiChatDataSource.startConnection()
        Log.d(TAG, "Started connection")
    }

    fun stopConnection() {
        apiChatDataSource.stopConnection()
        Log.d(TAG, "Stopped connection")
    }

    fun sendMessage(receiverUserId: Int, message: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val result = apiChatDataSource.sendMessage(receiverUserId, message)) {
                    is ApiResult.Success -> {
                        val currentChatHistory = _chatHistory.value?.toMutableList() ?: mutableListOf()
                        val newMessage = ChatMessage(
                            chatMessageId = _chatHistory.value!!.size + 1,
                            content = message,
                            dateTime = LocalDateTime.now().toString(),
                            senderId = 0
                        )
                        currentChatHistory.add(newMessage)
                        _chatHistory.postValue(currentChatHistory)
                        callback(true)
                        Log.d(TAG, "Message sent successfully to $receiverUserId")
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

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        return sdf.format(Date())
    }
}
