package com.example.flats4us21.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.services.*
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "UserViewModel"
class UserViewModel: ViewModel() {
    private val apiSurveyRepository : StudentSurveyService = StudentSurveyService
    private val userRepository : UserDataSource = ApiUserDataSource
    private val interestRepository: InterestDataSource = ApiInterestDataSource
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

    private var _profilePicture: Uri? = null
    var profilePicture: Uri?
        get() = _profilePicture
        set(value) {
            _profilePicture = value
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

    private var _address: String = ""
    var address: String
        get() = _address
        set(value) {
            _address = value
        }

    private var _phoneNumber: String = ""
    var phoneNumber: String
        get() = _phoneNumber
        set(value) {
            _phoneNumber = value
        }

    private var _links: MutableList<String> = mutableListOf()
    var links: MutableList<String>
        get() = _links
        set(value) {
            _links = value
        }

    private var _interest: MutableList<Int> = mutableListOf()
    var interest: MutableList<Int>
        get() = _interest
        set(value) {
            _interest = value
        }

    private val _interests = MutableLiveData<List<Interest>>()
    val interests: LiveData<List<Interest>>
        get() = _interests

    fun getInterests(){
        _isLoading.value = true
        viewModelScope.launch {
            val fetchedInterests = interestRepository.getInterests()
            Log.i(TAG, "Fetched list of interest: $fetchedInterests")
            _interests.value = fetchedInterests
            _isLoading.value = false
        }
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

    private var _documentType: DocumentType? = null
    var documentType: DocumentType?
        get() = _documentType
        set(value) {
            _documentType = value
        }

    private var _documentExpireDate: LocalDate? = null
    var documentExpireDate: LocalDate?
        get() = _documentExpireDate
        set(value) {
            _documentExpireDate = value
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

    private var _images: MutableList<Uri> = mutableListOf()
    var images: MutableList<Uri>
        get() = _images
        set(value) {
            _images = value
        }

    fun createUser() {
        val newUser = NewUserDto(
            userType,
            profilePicture,
            name,
            surname,
            address,
            phoneNumber,
            links,
            interest,
            birthDate,
            university,
            studentNumber,
            bankAccount,
            questionAccount
        )
    }

    fun clearData(){
        userType= null
        _name = ""
        _surname = ""
        _address = ""
        _phoneNumber = ""
        _birthDate = ""
        _university = ""
        _studentNumber = ""
        _bankAccount = ""
        _questionResponseList = listOf()
        _images = mutableListOf()
    }

}
