package com.example.flats4us21.services

import com.example.flats4us21.data.RentalPlace

interface MapDataSource {
    fun getRentalPlaces(): List<RentalPlace>
}
