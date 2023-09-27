package com.example.flats4us21.services

import com.example.flats4us21.R
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType

object HardcodedOfferDataSource : OfferDataSource {
    private val offers : MutableList<Offer> = mutableListOf()

    init{
        offers.add(
            Offer(
                dateIssue = "20-03-2023",
                status = "aktywny",
                price = "%.2f".format(2500.50F),
                description = "Lorem ipsum",
                rentalPeriod = "3",
                interestedPeople = 48,
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
        )
        offers.add(
            Offer(
                dateIssue = "21-03-2023",
                status = "aktywny",
                price = "%.2f".format(3500.50F),
                description = "Lorem ipsum",
                rentalPeriod = "2",
                interestedPeople = 18,
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
        )

        offers.add(
            Offer(
                dateIssue = "21-03-2023",
                status = "aktywny",
                price = "%.2f".format(3500.50F),
                description = "Lorem ipsum",
                rentalPeriod = "2",
                interestedPeople = 18,
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
        )
    }

    override fun getOffers(): List<Offer> {
        return offers
    }

    override fun addOffer(offer: Offer) {
        offers.add(offer)
    }

}