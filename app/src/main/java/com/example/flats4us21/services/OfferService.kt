package com.example.flats4us21.services

import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.NewOfferDto
import retrofit2.Response
import retrofit2.http.*

interface OfferService {
    @GET("/api/offers")
    suspend fun getOffers(
        @Query("Sorting") sorting: String?,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Query("City") city: String?,
        @Query("Distnace") distnace: Int?,
        @Query("PropertyType") propertyType: Int?,
        @Query("MinPrice") minPrice: Int?,
        @Query("MaxPrice") maxPrice: Int?,
        @Query("District") district: String?,
        @Query("MinArea") minArea: Int?,
        @Query("MaxArea") maxArea: Int?,
        @Query("MinYear") minYear: Int?,
        @Query("MaxYear") maxYear: Int?,
        @Query("MinNumberOfRooms") minNumberOfRooms: Int?,
        @Query("Floor") floor: Int?,
        @Query("Equipment") equipment: List<Int>?
    ): Response<OffersResult>

    @GET("/api/offers/{id}")
    suspend fun getOffer(@Path("id") offerId: Int): Response<Offer>

    @GET("/api/offers/mine")
    suspend fun getMineOffers(): Response<OffersResult>

    @POST("/api/offers")
    suspend fun createOffer(@Body newOffer : NewOfferDto): Response<NewPropertyApiResponse<String>>

    @POST("/api/offers/{offerId}/rent")
    suspend fun addRentProposition(@Path("offerId") offerId: Int, rentProposition: RealEstateRental): Response<NewPropertyApiResponse<String>>
}