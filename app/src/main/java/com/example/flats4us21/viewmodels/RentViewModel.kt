package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Rent
import com.example.flats4us21.services.ApiOfferDataSource
import com.example.flats4us21.services.OfferDataSource
import kotlinx.coroutines.launch

private const val TAG = "OfferViewModel"
class RentViewModel: ViewModel() {
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
        get() = _resultMessage

    private val _rents: MutableLiveData<MutableList<Rent>> = MutableLiveData()
    val rents: LiveData<MutableList<Rent>>
        get() = _rents

    private val _rent: MutableLiveData<Rent?> = MutableLiveData()
    val rent: LiveData<Rent?>
        get() = _rent

    fun getRents() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedRents = apiOfferRepository.getRents()
                Log.i(TAG, "Fetched rents: $fetchedRents")
                when (fetchedRents) {
                    is ApiResult.Success -> {
                        val data = fetchedRents.data.result as MutableList<Rent>
                        _rents.value = data
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

    fun getRent(rentId : Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.getRent(rentId)) {
                    is ApiResult.Success -> {
                        _rent.value = response.data
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
}