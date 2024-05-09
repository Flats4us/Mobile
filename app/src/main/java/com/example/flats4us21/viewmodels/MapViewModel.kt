package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.FilterCriteria
import com.example.flats4us21.data.Offer
import com.example.flats4us21.services.MapDataSource
import com.example.flats4us21.services.HardcodedMapDataSource

class MapViewModel : ViewModel() {
    private val mapRepository: MapDataSource = HardcodedMapDataSource()
    private var _selectedOffer: Offer? = null
    var selectedOffer: Offer?
        get() = _selectedOffer
        set(value) {
            _selectedOffer = value
        }

    fun loadOffers(filter: FilterCriteria? = null): List<Offer> {
        val offers = mapRepository.getOffers()
        return filter?.let {
            offers.filter { offer ->
                val property = offer.property
                (filter.location == null || property.city.contains(filter.location, ignoreCase = true)) &&
                        (filter.minSize == null || property.area >= filter.minSize) &&
                        (filter.maxSize == null || property.area <= filter.maxSize) &&
                        (filter.rooms == null || property.numberOfRooms == filter.rooms) &&
                        // Remove or modify the following lines as they refer to numberOfFloors
                        //(filter.floor == null || property.numberOfFloors == filter.floor) &&
                        //(filter.minFloor == null || property.numberOfFloors >= filter.minFloor) &&
                        //(filter.maxFloor == null || property.numberOfFloors <= filter.maxFloor) &&
                        (filter.minResidents == null || property.maxResidents >= filter.minResidents) &&
                        (filter.maxResidents == null || property.maxResidents <= filter.maxResidents)
            }
        } ?: offers
    }

}
