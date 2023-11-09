package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.RentalPlace

interface MapDataSource {
    fun getOffers(): List<Offer>
}