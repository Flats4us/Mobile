package com.example.flats4us21.services

import com.example.flats4us21.data.Offer

interface OfferDataSource {
    fun getOffers() : List<Offer>
    fun getWatchedOffers(): List<Offer>
    fun addOffer(offer: Offer)
    fun addOfferToWatched(offer: Offer)
    fun removeOfferToWatched(offer: Offer)
}