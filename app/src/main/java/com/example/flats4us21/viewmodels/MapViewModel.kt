package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.RentalPlace
import com.example.flats4us21.services.MapDataSource
import com.example.flats4us21.services.HardcodedMapDataSource
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {
    private val mapRepository: MapDataSource = HardcodedMapDataSource()

    fun loadData(): List<RentalPlace> {
        return mapRepository.getRentalPlaces()
    }

}
