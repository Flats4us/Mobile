package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.services.*
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "OfferViewModel"
class OfferViewModel: ViewModel() {
    private val apiPropertyRepository : PropertyDataSource = ApiPropertyDataSource
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

    private val _offers: MutableLiveData<List<Offer>> = MutableLiveData()
    val offers: LiveData<List<Offer>>
        get() = _offers

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _userProperties: MutableLiveData<List<Property>> = MutableLiveData()
    val userProperties: LiveData<List<Property>>
        get() = _userProperties

    fun getUserProperties(){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedProperties = apiPropertyRepository.getUserProperties()
                Log.d(TAG, "[getUserProperties] Fetched properties: $fetchedProperties")
                _userProperties.value = fetchedProperties
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private var _price : Double = 0.0
    var price: Double
    get() = _price
    set(value) {
        _price = value
    }

    private var _rentalPeriod: Int = 0
    var rentalPeriod: Int
    get() = _rentalPeriod
    set(value) {
        _rentalPeriod = value
    }

    private var _description: String = ""
    var description: String
    get() = _description
    set(value) {
        _description = value
    }

    private var _property: Property? = null
    var property: Property?
    get() = _property
    set(value) {
        _property = value
    }

    private var _rules: String = ""
    var rules: String
    get() = _rules
    set(value) {
        _rules = value
    }

    private var _selectedOffer: Offer? = null
    var selectedOffer: Offer?
    get() = _selectedOffer
    set(value) {
        _selectedOffer = value
    }

     fun createOffer(){
        val offer = NewOfferDto(
            LocalDate.now().toString(),
            price.toString(),
            description,
            rentalPeriod.toString(),
            null,
            property!!.propertyId
        )
         viewModelScope.launch {
             apiOfferRepository.addOffer(offer)
         }
    }

    fun getWatchedOffers(): List<Offer>{
        var offers: MutableList<Offer> = mutableListOf()
        viewModelScope.launch {
            offers = apiOfferRepository.getWatchedOffers() as MutableList<Offer>
        }
        return offers
    }

    fun getOffers() {
         viewModelScope.launch {
             _errorMessage.value = null
             _isLoading.value = true
             try{
                 val fetchedOffers = apiOfferRepository.getOffers()
                 Log.i(TAG, "Fetched offers: $fetchedOffers")
                 _offers.value = fetchedOffers
             } catch (e: Exception) {
                 _errorMessage.value = e.message
                 Log.e(TAG, "Exception $e")
             } finally {
                 _isLoading.value = false
             }
        }
    }


    fun checkIfIsWatched(offer: Offer): Boolean{
        var offers: MutableList<Offer> = mutableListOf()
        viewModelScope.launch {
            offers = apiOfferRepository.getWatchedOffers() as MutableList<Offer>
        }
        return offers.contains(offer)
    }

    fun watchOffer(offer: Offer){
        apiOfferRepository.addOfferToWatched(offer)
    }

    fun unwatchOffer(offer: Offer){
        apiOfferRepository.removeOfferToWatched(offer)
    }

    fun getLastViewedOffers(): List<Offer>{
        var offer: MutableList<Offer> = mutableListOf()
        viewModelScope.launch {
            offer = apiOfferRepository.getLastViewedOffers() as MutableList<Offer>
        }
        return offer
    }

    fun addOfferToLastViewed(offer: Offer?){
        if (offer != null) {
            apiOfferRepository.addOfferToLastViewed(offer)
        }
    }
}