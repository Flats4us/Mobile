package com.example.flats4us21.services

import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.utils.FileUtils

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
            image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg()),
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
                image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg(), FileUtils.getUriOfExamplePropertyJpg(), FileUtils.getUriOfExamplePropertyJpg())
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
                image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg()),
            )
        )
    }

    override fun getUserProperties(): List<Property> {
        return properties
    }
}