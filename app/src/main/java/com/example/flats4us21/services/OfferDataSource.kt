package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto

interface OfferDataSource {
    suspend fun getOffers() : List<Offer>
    fun getWatchedOffers(): List<Offer>
    suspend fun addOffer(offer: NewOfferDto)
    fun addOfferToWatched(offer: Offer)
    fun removeOfferToWatched(offer: Offer)
    fun getLastViewedOffers(): List<Offer>
    fun addOfferToLastViewed(offer: Offer)
}