package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.OffersResult
import com.example.flats4us21.data.RealEstateRental
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.OfferFilter

interface OfferDataSource {
    suspend fun getOffers(offerFilter: OfferFilter) : ApiResult<OffersResult>
    suspend fun getMineOffers() : ApiResult<List<Offer>>
    suspend fun getWatchedOffers(pageNumber: Int, pageSize: Int): ApiResult<OffersResult>
    suspend fun createOffer(offer: NewOfferDto) : ApiResult<String>
    suspend fun addOfferToWatched(offerId: Int) : ApiResult<String>
    suspend fun removeOfferToWatched(offerId: Int) : ApiResult<String>
    suspend fun getLastViewedOffers(): List<Offer>
    fun addOfferToLastViewed(offer: Offer)
    suspend fun getOffer(offerId: Int) : ApiResult<Offer>
    suspend fun addRentProposition(offerId: Int, rentProposition: RentProposition): ApiResult<String>
}