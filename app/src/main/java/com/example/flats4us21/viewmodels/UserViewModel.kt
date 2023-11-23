package com.example.flats4us21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flats4us21.services.ApiUserDataSource
import com.example.flats4us21.services.UserDataSource

private const val TAG = "UserViewModel"
class UserViewModel: ViewModel() {
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
}