package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.utils.FileUtils

object HardcodedOfferDataSource : OfferDataSource {
    private val offers : MutableList<Offer> = mutableListOf()
    private val watchedOffers : MutableList<Offer> = mutableListOf()
    private val lastViewed : MutableList<Offer> = mutableListOf()

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
                    equipment = mutableListOf(),
                    image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg()),
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
                    equipment = mutableListOf("Sofa"),
                    image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg(), FileUtils.getUriOfExamplePropertyJpg(), FileUtils.getUriOfExamplePropertyJpg())
                )
            )
        )

        val offer = Offer(
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
                equipment = mutableListOf("Sofa"),
                image = mutableListOf(FileUtils.getUriOfExamplePropertyJpg()),
            )
        )
        offers.add(offer)
        watchedOffers.add(offer)
    }

    override fun getOffers(): List<Offer> {
        return offers
    }

    override fun getWatchedOffers(): List<Offer> {
        return watchedOffers
    }

    override fun addOffer(offer: Offer) {
        offers.add(offer)
    }

    override fun addOfferToWatched(offer: Offer) {
        watchedOffers.add(offer)
    }

    override fun removeOfferToWatched(offer: Offer) {
        if(watchedOffers.contains(offer)){
            watchedOffers.remove(offer)
        }
    }

    override fun getLastViewedOffers(): List<Offer> {
         return lastViewed.reversed()
    }

    override fun addOfferToLastViewed(offer: Offer) {
        if(!lastViewed.contains(offer)){
            lastViewed.add(offer)
        } else {
            lastViewed.remove(offer)
            lastViewed.add(offer)
        }
    }
}