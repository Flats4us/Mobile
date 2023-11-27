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
}