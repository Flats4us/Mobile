package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.services.ApiMatcherDataSource
import com.example.flats4us21.services.MatcherDataSource
import kotlinx.coroutines.launch

private const val TAG = "MatcherViewModel"
class MatcherViewModel : ViewModel() {
    private val matcherRepository : MatcherDataSource = ApiMatcherDataSource()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    private val _potentialMatches: MutableLiveData<MutableList<StudentForMatcher>> = MutableLiveData()
    val potentialMatches: LiveData<MutableList<StudentForMatcher>>
        get() = _potentialMatches

    private val _existingMatches: MutableLiveData<MutableList<StudentForMatcher>> = MutableLiveData()
    val existingMatches: LiveData<MutableList<StudentForMatcher>>
        get() = _existingMatches

    fun getPotentialMatches() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedRents = matcherRepository.getPotentialMatches()
                Log.i(TAG, "Fetched potential matches: $fetchedRents")
                when (fetchedRents) {
                    is ApiResult.Success -> {
                        val data = fetchedRents.data as MutableList<StudentForMatcher>
                        _potentialMatches.value = data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedRents.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
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

    fun getExistingMatches() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedRents = matcherRepository.getExistingMatches()
                Log.i(TAG, "Fetched existing matches: $fetchedRents")
                when (fetchedRents) {
                    is ApiResult.Success -> {
                        val data = fetchedRents.data as MutableList<StudentForMatcher>
                        _existingMatches.value = data
                        Log.i(TAG, "Existing matches: ${_existingMatches.value}")
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedRents.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
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

    fun addMatchDecision(studentId: Int, decision: Boolean) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val matchDecision = RentDecision(decision)
                val fetchedRents = matcherRepository.acceptPotentialMatch(studentId, matchDecision)
                Log.i(TAG, "Fetched existing matches: $fetchedRents")
                when (fetchedRents) {
                    is ApiResult.Success -> {
                        val data = fetchedRents.data
                        _resultMessage.value = data
                        getPotentialMatches()
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedRents.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
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