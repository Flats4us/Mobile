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

class OfferViewModel: ViewModel() {
    private val apiPropertyRepository : PropertyDataSource = ApiPropertyDataSource
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

    private val _offers: MutableLiveData<List<Offer>> = MutableLiveData()
    val offers: LiveData<List<Offer>>
        get() = _offers

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getUserProperties(): MutableList<Property>{
        var property: MutableList<Property> = mutableListOf()
        viewModelScope.launch {
            property = apiPropertyRepository.getUserProperties() as MutableList<Property>
        }
        return property
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
             _isLoading.value = true
            val fetchedOffers = apiOfferRepository.getOffers()
             Log.i("OfferViewModel", "Fetched offers: $fetchedOffers")
             _offers.value = fetchedOffers
             _isLoading.value = false
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