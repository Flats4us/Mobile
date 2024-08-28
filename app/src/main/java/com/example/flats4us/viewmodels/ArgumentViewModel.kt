package com.example.flats4us.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Argument
import com.example.flats4us.data.dto.NewArgumentDto
import com.example.flats4us.services.ApiArgumentDataSource
import com.example.flats4us.services.ArgumentDataSource
import kotlinx.coroutines.launch

private const val TAG = "ArgumentViewModel"
class ArgumentViewModel: ViewModel() {
    private val argumentRepository : ArgumentDataSource = ApiArgumentDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _userArguments = MutableLiveData<List<Argument>>()
    val userArguments: LiveData<List<Argument>> get() = _userArguments

    fun getArgument() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = argumentRepository.getArguments()) {
                    is ApiResult.Success -> {
                        val data = fetchedData.data
                        _userArguments.value = data
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedData.message}")
                        _errorMessage.value = fetchedData.message
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

    fun addArgument(argument: NewArgumentDto, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = argumentRepository.addArgument(argument)) {
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

    fun ownerAcceptArgument(id: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = argumentRepository.ownerAcceptArgument(id)) {
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

    fun studentAcceptArgument(argumentId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = argumentRepository.studentAcceptArgument(argumentId)) {
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

    fun askingForIntervention(id: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = argumentRepository.askingForIntervention(id)) {
                    is ApiResult.Success -> {
                        val data = fetchedData.data
                        _userArguments.value = _userArguments.value!!.map {
                            if (it.argumentId == id) {
                                it.copy(interventionNeed = true)
                            } else {
                                it
                            }
                        }
                        Log.i(TAG, "SUCCESS: ${_userArguments.value!!.find { it.argumentId == id }}")
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

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}