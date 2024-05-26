package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.services.StudentSurveyService
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
class MainViewModel : ViewModel() {
    private val apiSurveyRepository : StudentSurveyService = StudentSurveyService

    private val _questionList: MutableLiveData<List<SurveyQuestion>> = MutableLiveData()
    val questionList: LiveData<List<SurveyQuestion>>
        get() = _questionList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

}