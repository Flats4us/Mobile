package com.example.flats4us21.services

import com.example.flats4us21.data.RentalPlace
import org.osmdroid.util.GeoPoint

class HardcodedMapDataSource : MapDataSource {
    private val rentals: List<RentalPlace> = listOf(
        RentalPlace("Apartament w Warszawie", "Warszawa, Polska", "Opis Apartamentu w Warszawie", mutableListOf()),
        RentalPlace("Dom w Krakowie", "Kraków, Polska", "Opis Domu w Krakowie", mutableListOf()),
        RentalPlace("Kawalerka w Poznaniu", "Poznań, Polska", "Opis Kawalerki w Poznaniu", mutableListOf()),
        RentalPlace("Mieszkanie w Gdańsku", "Gdańsk, Polska", "Opis Mieszkania w Gdańsku", mutableListOf())
    )

    override fun getRentalPlaces(): List<RentalPlace> {
        return rentals
    }
}

