package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.FilterCriteria
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.RentalPlace
import com.example.flats4us21.services.MapDataSource
import com.example.flats4us21.services.HardcodedMapDataSource
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {
    private val mapRepository: MapDataSource = HardcodedMapDataSource()

    fun loadData(filter: FilterCriteria? = null): List<Property> {
        val properties = mapRepository.getProperties()
        if (filter == null) return properties

        return properties.filter {
            (filter.location == null || it.city.contains(filter.location, true))
                    && (filter.minSize == null || it.area >= filter.minSize)
                    && (filter.maxSize == null || it.area <= filter.maxSize)
                    && (filter.rooms == null || it.numberOfRooms == filter.rooms)
                    && (filter.floor == null || it.numberOfFloors == filter.floor)
                    && (filter.minFloor == null || it.numberOfFloors >= filter.minFloor)
                    && (filter.maxFloor == null || it.numberOfFloors <= filter.maxFloor)
                    && (filter.minResidents == null || it.maxResidents >= filter.minResidents)
                    && (filter.maxResidents == null || it.maxResidents <= filter.maxResidents)
        }
    }

}