package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MapOffersResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.OffersResult
import com.example.flats4us21.data.Rent
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.NewRentProposition
import com.example.flats4us21.data.dto.OfferFilter
import com.example.flats4us21.data.utils.RentResult

interface OfferDataSource {
    suspend fun getOffers(offerFilter: OfferFilter) : ApiResult<OffersResult>
    suspend fun getOffersForMap(offerFilter: OfferFilter) : ApiResult<MapOffersResult>
    suspend fun getMineOffers() : ApiResult<List<Offer>>
    suspend fun getWatchedOffers(pageNumber: Int, pageSize: Int): ApiResult<OffersResult>
    suspend fun createOffer(offer: NewOfferDto) : ApiResult<String>
    suspend fun addOfferToWatched(offerId: Int) : ApiResult<String>
    suspend fun removeOfferToWatched(offerId: Int) : ApiResult<String>
    suspend fun getOffer(offerId: Int) : ApiResult<Offer>
    suspend fun addRentProposition(offerId: Int, rentProposition: NewRentProposition): ApiResult<String>
    suspend fun getRentProposition(rentId: Int): ApiResult<RentProposition>
    suspend fun addRentDecision(offerId: Int, decision : Boolean) : ApiResult<String>
    suspend fun getRents(): ApiResult<RentResult>
    suspend fun getRent(rentId: Int): ApiResult<Rent>
}
