package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.services.HardcodedOfferDataSource
import com.example.flats4us21.services.HardcodedPropertyDataSource
import com.example.flats4us21.services.OfferDataSource
import com.example.flats4us21.services.PropertyDataSource
import java.time.LocalDate

class OfferViewModel: ViewModel() {
    private val propertyRepository : PropertyDataSource = HardcodedPropertyDataSource()
    private val offerRepository : OfferDataSource = HardcodedOfferDataSource

    fun getUserProperties(): List<Property>{
        return propertyRepository.getUserProperties()
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

     fun createOffer(){
        val offer = Offer(
            LocalDate.now().toString(),
            "aktywny",
            price.toString(),
            description,
            rentalPeriod.toString(),
            0,
            property!!
        )
        offerRepository.addOffer(offer)
    }
}