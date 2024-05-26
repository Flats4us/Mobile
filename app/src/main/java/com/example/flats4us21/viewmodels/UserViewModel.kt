package com.example.flats4us21.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.DocumentType
import com.example.flats4us21.data.Interest
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.data.UserMenuData
import com.example.flats4us21.data.UserType
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.data.dto.UpdateMyProfileDto
import com.example.flats4us21.services.ApiInterestDataSource
import com.example.flats4us21.services.ApiUserDataSource
import com.example.flats4us21.services.InterestDataSource
import com.example.flats4us21.services.StudentSurveyService
import com.example.flats4us21.services.UserDataSource
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

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    private val _loginResponse = MutableLiveData<LoginResponse?>(null)
    val loginResponse: LiveData<LoginResponse?>
        get() = _loginResponse

    private val _myProfile = MutableLiveData<MyProfile?>(null)
    val myProfile: LiveData<MyProfile?>
        get() = _myProfile

    private val _profile = MutableLiveData<Profile?>(null)
    val profile: LiveData<Profile?>
        get() = _profile

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

    private var _birthDate: LocalDate? = null
    var birthDate: LocalDate?
        get() = _birthDate
        set(value) {
            _birthDate = value
        }

    private var _university: String? = null
    var university: String?
        get() = _university
        set(value) {
            _university = value
        }

    private var _studentNumber: String? = null
    var studentNumber: String?
        get() = _studentNumber
        set(value) {
            _studentNumber = value
        }

    private var _bankAccount: String? = null
    var bankAccount: String?
        get() = _bankAccount
        set(value) {
            _bankAccount = value
        }

    private var _documentNumber: String? = null
    var documentNumber: String?
        get() = _documentNumber
        set(value) {
            _documentNumber = value
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

    private val _questionList: MutableLiveData<MutableList<SurveyQuestion>> = MutableLiveData()
    val questionList: MutableLiveData<MutableList<SurveyQuestion>>
        get() = _questionList

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

    private var _email: String = ""
    var email: String
        get() = _email
        set(value) {
            _email = value
        }

    private var _password: String = ""
    var password: String
        get() = _password
        set(value) {
            _password = value
        }

    private var _repeatPassword: String = ""
    var repeatPassword: String
        get() = _repeatPassword
        set(value) {
            _repeatPassword = value
        }

    private var _user: UserMenuData? = null
    var user: UserMenuData?
        get() = _user
        set(value) {
            _user = value
        }

    fun getInterests(){
        _isLoading.value = true
        viewModelScope.launch {
            val fetchedInterests = interestRepository.getInterests()
            Log.i(TAG, "Fetched list of interest: $fetchedInterests")
            _interests.value = fetchedInterests
            _isLoading.value = false
        }
    }

    fun getQuestionList(surveyType: String){
        _errorMessage.value = null
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val fetchedQuestions = apiSurveyRepository.getSurveyQuestion(surveyType)
                Log.i(TAG, "Fetched questions: $fetchedQuestions")
                when(fetchedQuestions) {
                    is ApiResult.Success -> {
                        val questions = fetchedQuestions.data
                        questionList.value = questions as MutableList<SurveyQuestion>
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedQuestions.message}")
                        _errorMessage.value = fetchedQuestions.message
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

    fun createUser() {
        val newUser = NewUserDto(
            UserType.valueOf(userType!!),
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
            documentNumber,
            documentExpireDate,
            questionResponseList,
            images,
            email,
            password,
            repeatPassword
        )
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                userRepository.register(newUser)
                Log.d(TAG, "[createUser] New User to create $newUser")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "[createUser] Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearData(){
        userType= null
        _name = ""
        _surname = ""
        _address = ""
        _phoneNumber = ""
        _birthDate = null
        _university = null
        _studentNumber =null
        _bankAccount = null
        documentNumber = null
        documentExpireDate = null
        _questionResponseList = listOf()
        _images = mutableListOf()
        email = ""
        password = ""
        repeatPassword = ""
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedLoginResponse = userRepository.login(email, password)
                Log.i(TAG, "Fetched loginResponse: $fetchedLoginResponse")
                when (fetchedLoginResponse) {
                    is ApiResult.Success -> {
                        Log.i(TAG, "Success")
                        val data = fetchedLoginResponse.data
                        _loginResponse.value = data
                        loginResponse.value?.let { DataStoreManager.saveUserData(it) }
                        when(val fetchedProfile = userRepository.getProfile()) {
                            is ApiResult.Success -> {
                                val profileData = fetchedProfile.data
                                _myProfile.value = profileData
                                Log.i(TAG, profileData.toString())
                                Log.i(TAG, _myProfile.value.toString())
                            }
                            is ApiResult.Error -> {
                                Log.i(TAG, "ERROR: ${fetchedProfile.message}")
                                _errorMessage.value = fetchedProfile.message
                                Log.e(TAG, "error: ${errorMessage.value}")
                            }
                        }
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedLoginResponse.message}")
                        _errorMessage.value = fetchedLoginResponse.message
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

    fun getMyProfile(){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedProfile = userRepository.getProfile()) {
                    is ApiResult.Success -> {
                        val profileData = fetchedProfile.data
                        _myProfile.value = profileData
                        Log.i(TAG, profileData.toString())
                        Log.i(TAG, _myProfile.value.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedProfile.message}")
                        _errorMessage.value = fetchedProfile.message
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

    fun getProfile(id: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetchedProfile = userRepository.getProfile(id)) {
                    is ApiResult.Success -> {
                        val profileData = fetchedProfile.data
                        _profile.value = profileData
                        Log.i(TAG, profileData.toString())
                        Log.i(TAG, _myProfile.value.toString())
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetchedProfile.message}")
                        _errorMessage.value = fetchedProfile.message
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

    fun sendPasswordResetLink(email: String, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                when(val fetched = userRepository.sendPasswordResetLink(email)) {
                    is ApiResult.Success -> {
                        val profileData = fetched.data
                        Log.i(TAG, "Fetched Data: $profileData")
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetched.message}")
                        _errorMessage.value = fetched.message
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

    fun updateMyProfile(callback: (Boolean) -> Unit){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val updatedMyProfileDto = UpdateMyProfileDto(
                    address,
                    bankAccount,
                    if (birthDate == null) null else birthDate.toString(),
                    if (documentExpireDate == null) null else documentExpireDate.toString(),
                    documentNumber,
                    email,
                    links,
                    name,
                    phoneNumber,
                    studentNumber,
                    surname,
                    university
                )
                when(val fetched = userRepository.updateMyProfile(updatedMyProfileDto)) {
                    is ApiResult.Success -> {
                        val fetchedData = fetched.data
                        Log.i(TAG, "Fetched Data: $fetchedData")
                        _resultMessage.value = fetchedData
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        Log.i(TAG, "ERROR: ${fetched.message}")
                        _errorMessage.value = fetched.message
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
}
