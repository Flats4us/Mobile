package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.data.dto.NewRentProposition
import com.example.flats4us21.services.ApiOfferDataSource
import com.example.flats4us21.services.ApiUserDataSource
import com.example.flats4us21.services.OfferDataSource
import com.example.flats4us21.services.UserDataSource
import kotlinx.coroutines.launch


private const val TAG = "DetailOfferViewModel"
class DetailOfferViewModel: ViewModel() {
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource
    private val apiUserRepository : UserDataSource = ApiUserDataSource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _offer = MutableLiveData<Offer>()
    val offer: LiveData<Offer>
        get() = _offer

    private val _rent = MutableLiveData<RentProposition>()
    val rent: LiveData<RentProposition>
        get() = _rent

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    private val _newRent = MutableLiveData<NewRentProposition?>(null)
    val newRent : LiveData<NewRentProposition?>
        get() = _newRent

    fun setRentValue(newValue: NewRentProposition?) {
        _newRent.value = newValue
    }

    fun getOfferDetails(offerId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedOffer = apiOfferRepository.getOffer(offerId)
                Log.i(TAG, "Fetched offers: $fetchedOffer")
                when (fetchedOffer) {
                    is ApiResult.Success -> {
                        val data = fetchedOffer.data
                        _offer.value = data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedOffer.message
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

    fun addRentProposition(offerId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val rentProposition = newRent.value!!
                val response = apiOfferRepository.addRentProposition(offerId, rentProposition)
                when (response) {
                    is ApiResult.Success -> {
                        Log.d(TAG, "Add rent proposition")
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
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

    fun checkEmail(email: String, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val response = apiUserRepository.checkEmail(email)
                when (response) {
                    is ApiResult.Success -> {
                        Log.d(TAG, "Does this email: $email exist? ${response.data}")
                        callback(response.data)
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
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

    fun watchOffer(offerId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _resultMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.addOfferToWatched(offerId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, response.data)
                        _resultMessage.value = response.data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
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

    fun unwatchOffer(offerId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _resultMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.removeOfferToWatched(offerId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, response.data)
                        _resultMessage.value = response.data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
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

    fun getRentProposition(rentId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedRent = apiOfferRepository.getRentProposition(rentId)
                Log.i(TAG, "Fetched offers: $fetchedRent")
                when (fetchedRent) {
                    is ApiResult.Success -> {
                        val data = fetchedRent.data
                        _rent.value = data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedRent.message
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

    fun addRentDecision(rentId: Int, decision: Boolean, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val response = apiOfferRepository.addRentDecision(rentId, decision)
                Log.i(TAG, "Fetched offers: $response")
                when (response) {
                    is ApiResult.Success -> {
                        callback(true)
                        _resultMessage.value = response.data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
                        callback(false)
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