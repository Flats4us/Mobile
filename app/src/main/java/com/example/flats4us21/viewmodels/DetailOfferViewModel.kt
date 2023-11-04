package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.Offer
import com.example.flats4us21.services.ApiOfferDataSource
import com.example.flats4us21.services.OfferDataSource
import kotlinx.coroutines.launch


private const val TAG = "DetailOfferViewModel"
class DetailOfferViewModel: ViewModel() {
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _offer = MutableLiveData<Offer>()
    val offer: LiveData<Offer>
        get() = _offer

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun getOfferDetails(offerId: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedOffer = apiOfferRepository.getOffer(offerId)
                Log.i(TAG, "Fetched offer: $fetchedOffer")
                _offer.value = fetchedOffer
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
}