package com.example.flats4us21.viewmodels

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
    private val offerRepository : OfferDataSource = HardcodedOfferDataSource
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

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
    }

    fun getWatchedOffers(): List<Offer>{
        return offerRepository.getWatchedOffers()
    }

    fun getOffers() : List<Offer>{
        var offer: MutableList<Offer> = mutableListOf()
        viewModelScope.launch {
            offer = apiOfferRepository.getOffers() as MutableList<Offer>
        }
        return offer
    }

    fun checkIfIsWatched(offer: Offer): Boolean{
        return offerRepository.getWatchedOffers().contains(offer)
    }

    fun watchOffer(offer: Offer){
        offerRepository.addOfferToWatched(offer)
    }

    fun unwatchOffer(offer: Offer){
        offerRepository.removeOfferToWatched(offer)
    }

    fun getLastViewedOffers(): List<Offer>{
        return offerRepository.getLastViewedOffers()
    }

    fun addOfferToLastViewed(offer: Offer?){
        if (offer != null) {
            offerRepository.addOfferToLastViewed(offer)
        }
    }
}