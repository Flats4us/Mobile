package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.services.ApiOfferDataSource
import com.example.flats4us21.services.OfferDataSource
import kotlinx.coroutines.launch

private const val TAG = "WatchedOffersViewModel"
class WatchedOffersViewModel: ViewModel() {
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

    private val _watchedOffers: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    val watchedOffers: LiveData<MutableList<Offer>>
        get() = _watchedOffers

    private val _newOffers: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    val newOffers: LiveData<MutableList<Offer>>
        get() = _newOffers

    private var _pageNumber : Int = 1
    var pageNumber : Int
        get() = _pageNumber
        set(value) {
            _pageNumber = value
        }
    private var _pageSize : Int = 5
    var pageSize : Int
        get() = _pageSize
        set(value) {
            _pageSize = value
        }

    private var _offersNumber : Int = 0
    var offersNumber: Int
        get() = _offersNumber
        set(value) {
            _offersNumber = value
        }

    fun getWatchedOffers(){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                if(pageNumber == 1 && _watchedOffers.value != null) {
                    watchedOffers.value!!.clear()
                }
                val fetchedOffers = apiOfferRepository.getWatchedOffers(pageNumber, pageSize)
                Log.i(TAG, "Fetched offers: $fetchedOffers")
                when (fetchedOffers) {
                    is ApiResult.Success -> {
                        val data = fetchedOffers.data.result as MutableList<Offer>
                        val totalCount = fetchedOffers.data.totalCount
                        _newOffers.value = data
                        pageNumber++

                        if(_watchedOffers.value == null) {
                            _watchedOffers.value = data
                        } else {
                            _watchedOffers.value!!.addAll(data)
                        }
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedOffers.message
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

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearResultMessage() {
        _resultMessage.value = null
    }
}