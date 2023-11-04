package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto

interface OfferDataSource {
    suspend fun getOffers() : List<Offer>
    suspend fun getWatchedOffers(): List<Offer>
    suspend fun addOffer(offer: NewOfferDto)
    fun addOfferToWatched(offer: Offer)
    fun removeOfferToWatched(offer: Offer)
    suspend fun getLastViewedOffers(): List<Offer>
    fun addOfferToLastViewed(offer: Offer)
    suspend fun getOffer(offerId: Int) : Offer{
        return null!!
    }
}