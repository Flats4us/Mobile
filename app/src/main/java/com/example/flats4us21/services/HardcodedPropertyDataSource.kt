package com.example.flats4us21.services

import com.example.flats4us21.R
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType

class HardcodedPropertyDataSource : PropertyDataSource {
    private val properties : MutableList<Property> = mutableListOf()

    init {
        properties.add(
            Property(
            propertyType = PropertyType.FLAT,
            voivodeship = "Mazowieckie",
            city = "Warszawa",
            district = "Bielany",
            street = "Kościuszki",
            buildingNumber = "10A",
            area = 40,
            maxResidents = 2,
            constructionYear = 2010,
            numberOfRooms = 1,
            numberOfFloors = 1,
            equipment = "Brak",
            image = mutableListOf(R.drawable.property),
            )
        )
        properties.add(
            Property(
                propertyType = PropertyType.FLAT,
                voivodeship = "Mazowieckie",
                city = "Warszawa",
                district = "Bielany",
                street = "Pruszkowska",
                buildingNumber = "10A",
                area = 60,
                maxResidents = 4,
                constructionYear = 2010,
                numberOfRooms = 3,
                numberOfFloors = 1,
                equipment = "Sofa",
                image = mutableListOf(R.drawable.property, R.drawable.property, R.drawable.property)
            )
        )
        properties.add(
            Property(
                propertyType = PropertyType.FLAT,
                voivodeship = "Mazowieckie",
                city = "Warszawa",
                district = "Bielany",
                street = "Pruszkowska",
                buildingNumber = "10A",
                area = 50,
                maxResidents = 4,
                constructionYear = 2010,
                numberOfRooms = 2,
                numberOfFloors = 1,
                equipment = "Sofa",
                image = mutableListOf(R.drawable.property),
            )
        )
    }

    override fun getUserProperties(): List<Property> {
        return properties
    }
}