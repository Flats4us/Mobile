package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.services.ApiUserDataSource
import com.example.flats4us21.services.StudentSurveyService
import com.example.flats4us21.services.UserDataSource
import kotlinx.coroutines.launch

private const val TAG = "UserViewModel"
class UserViewModel: ViewModel() {
    private val apiSurveyRepository : StudentSurveyService = StudentSurveyService
    private val userRepository : UserDataSource = ApiUserDataSource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _userType: String? = null
    var userType: String?
        get() = _userType
        set(value) {
            _userType = value
        }

    private var _name: String = ""
    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    private var _surname: String = ""
    var surname: String
        get() = _surname
        set(value) {
            _surname = value
        }

    private var _city: String = ""
    var city: String
        get() = _city
        set(value) {
            _city = value
        }

    private var _street: String = ""
    var street: String
        get() = _street
        set(value) {
            _street = value
        }

    private var _buildingNumber: String = ""
    var buildingNumber: String
        get() = _buildingNumber
        set(value) {
            _buildingNumber = value
        }

    private var _flatNumber: String = ""
    var flatNumber: String
        get() = _flatNumber
        set(value) {
            _flatNumber = value
        }

    private var _postalCode: String = ""
    var postalCode: String
        get() = _postalCode
        set(value) {
            _postalCode = value
        }

    private var _phoneNumber: String = ""
    var phoneNumber: String
        get() = _phoneNumber
        set(value) {
            _phoneNumber = value
        }

    private var _birthDate: String = ""
    var birthDate: String
        get() = _birthDate
        set(value) {
            _birthDate = value
        }

    private var _university: String = ""
    var university: String
        get() = _university
        set(value) {
            _university = value
        }

    private var _studentNumber: String = ""
    var studentNumber: String
        get() = _studentNumber
        set(value) {
            _studentNumber = value
        }

    private var _bankAccount: String = ""
    var bankAccount: String
        get() = _bankAccount
        set(value) {
            _bankAccount = value
        }

    private val _questionList: MutableLiveData<List<SurveyQuestion>> = MutableLiveData()
    val questionList: LiveData<List<SurveyQuestion>>
        get() = _questionList
    fun getQuestionList(surveyType: String){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedQuestions = apiSurveyRepository.getSurveyQuestion(surveyType)
                Log.i(TAG, "Fetched questions: $fetchedQuestions")
                _questionList.value = fetchedQuestions
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private lateinit var _questionResponseList: List<QuestionResponse>
    var questionResponseList: List<QuestionResponse>
        get() = _questionResponseList
        set(value) {
            _questionResponseList = value
        }
}
