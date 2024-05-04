package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.RealEstateRental
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.OfferFilter

interface OfferDataSource {
    suspend fun getOffers(offerFilter: OfferFilter) : ApiResult<List<Offer>>
    suspend fun getMineOffers() : ApiResult<List<Offer>>
    suspend fun getWatchedOffers(): List<Offer>
    suspend fun createOffer(offer: NewOfferDto) : ApiResult<String>
    fun addOfferToWatched(offer: Offer)
    fun removeOfferToWatched(offer: Offer)
    suspend fun getLastViewedOffers(): List<Offer>
    fun addOfferToLastViewed(offer: Offer)
    suspend fun getOffer(offerId: Int) : ApiResult<Offer>
    suspend fun addRentProposition(offerId: Int, rentProposition: RealEstateRental): ApiResult<String>

}