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
class ChatViewModel: ViewModel() {
    private val chatRepository : ChatDataSource = ApiChatDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _chatMessages: MutableLiveData<MutableList<ChatMessage>> = MutableLiveData()
    val chatMessages: MutableLiveData<MutableList<ChatMessage>>
        get() = _chatMessages

    private val _chatParticipants: MutableLiveData<Int> = MutableLiveData()
    val chatParticipants: MutableLiveData<Int>
        get() = _chatParticipants

    private val _chats: MutableLiveData<MutableList<Chat>> = MutableLiveData()
    val chats: MutableLiveData<MutableList<Chat>>
        get() = _chats

    fun sendMessage(receiverUserId: Int, message: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = chatRepository.sendMessage(receiverUserId, message)) {
                    is ApiResult.Success -> {
                        val data = fetchedData.data
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedData.message}")
                        _errorMessage.value = fetchedData.message
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

    fun getChatHistory(chatId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedChatMessages = chatRepository.getChatHistory(chatId)) {
                    is ApiResult.Success -> {
                        val chatData = fetchedChatMessages.data
                        _chatMessages.value = chatData as MutableList<ChatMessage>
                        Log.i(TAG, chatData.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedChatMessages.message}")
                        _errorMessage.value = fetchedChatMessages.message
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

    fun getChatParticipants(chatId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedChatMessages = chatRepository.getChatParticipants(chatId)) {
                    is ApiResult.Success -> {
                        val profileData = fetchedChatMessages.data
                        _chatParticipants.value = profileData
                        Log.i(TAG, profileData.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedChatMessages.message}")
                        _errorMessage.value = fetchedChatMessages.message
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

    fun getUserChats() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedChats = chatRepository.getUserChats()) {
                    is ApiResult.Success -> {
                        val profileData = fetchedChats.data
                        _chats.value = profileData as MutableList<Chat>
                        Log.i(TAG, profileData.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedChats.message}")
                        _errorMessage.value = fetchedChats.message
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
}
