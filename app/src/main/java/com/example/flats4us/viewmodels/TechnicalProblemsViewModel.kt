package com.example.flats4us.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.NewTechnicalProblemsDto
import com.example.flats4us.services.ApiTechnicalProblemsDataSource
import com.example.flats4us.services.TechnicalProblemsDataSource
import kotlinx.coroutines.launch

private const val TAG = "ArgumentViewModel"
class TechnicalProblemsViewModel: ViewModel() {
    private val technicalProblemsRepository : TechnicalProblemsDataSource = ApiTechnicalProblemsDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    fun addTechnicalProblems(newTechnicalProblemsDto: NewTechnicalProblemsDto, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedData = technicalProblemsRepository.addTechnicalProblems(newTechnicalProblemsDto)) {
                    is ApiResult.Success -> {
                        val data = fetchedData.data
                        _resultMessage.value = data
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

    fun clearResultMessage() {
        _resultMessage.value = null
    }
}