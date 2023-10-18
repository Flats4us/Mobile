package com.example.flats4us21.services

import com.example.flats4us21.data.RentalPlace
import org.osmdroid.util.GeoPoint

class HardcodedMapDataSource : MapDataSource {
    private val rentals: List<RentalPlace> = listOf(
        RentalPlace("Apartament w Warszawie", 52.2297, 21.0122, "Opis Apartamentu w Warszawie", mutableListOf()),
        RentalPlace("Dom w Krakowie", 50.0647, 19.9450, "Opis Domu w Krakowie", mutableListOf()),
        RentalPlace("Kawalerka w Poznaniu", 52.4064, 16.9252, "Opis Kawalerki w Poznaniu", mutableListOf()),
        RentalPlace("Mieszkanie w Gdańsku", 54.3520, 18.6466, "Opis Mieszkania w Gdańsku", mutableListOf())
    )

    override fun getRentalPlaces(): List<RentalPlace> {
        return rentals
    }
}

