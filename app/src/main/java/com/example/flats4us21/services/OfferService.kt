package com.example.flats4us21.services

import com.example.flats4us21.data.MapOffersResult
import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.OffersResult
import com.example.flats4us21.data.Rent
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.NewRentProposition
import com.example.flats4us21.data.utils.RentResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferService {

    @GET("/api/offers/{id}")
    suspend fun getOffer(@Path("id") offerId: Int): Response<Offer>

    @GET("/api/offers")
    suspend fun getOffers(
        @Query("Sorting") sorting: String?,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Query("Province") province: String?,
        @Query("City") city: String?,
        @Query("Distance") distance: Int?,
        @Query("PropertyTypes") propertyType: Int?,
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

    @GET("/api/offers/map")
    suspend fun getOffersForMap(
        @Query("City") city: String?,
        @Query("Distance") distance: Int?,
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
    ): Response<MapOffersResult>

    @GET("/api/offers/mine")
    suspend fun getMineOffers(): Response<OffersResult>

    @POST("/api/offers")
    suspend fun createOffer(@Body newOffer : NewOfferDto): Response<NewPropertyApiResponse<String>>

    @POST("/api/offers/{offerId}/rent")
    suspend fun addRentProposition(@Path("offerId") offerId: Int, @Body rentProposition: NewRentProposition): Response<NewPropertyApiResponse<String>>

    @GET("/api/offers/interest")
    suspend fun getObservedOffers(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ): Response<OffersResult>

    @POST("/api/offers/{offerId}/interest")
    suspend fun addOfferInterest(@Path("offerId") offerId: Int): Response<NewPropertyApiResponse<String>>

    @DELETE("/api/offers/{offerId}/interest")
    suspend fun deleteOfferInterest(@Path("offerId") offerId: Int): Response<NewPropertyApiResponse<String>>

    @GET("/api/rent/{rentId}/proposition")
    suspend fun getRentProposition(@Path("rentId") rentId: Int): Response<RentProposition>

    @PUT("/api/offers/{offerId}/rent/accept")
    suspend fun addRentDecision(@Path("offerId") offerId: Int, @Body decision: RentDecision): Response<NewPropertyApiResponse<String>>

    @GET("/api/rent")
    suspend fun getRents(): Response<RentResult>

    @GET("/api/rent/{id}")
    suspend fun getRent(@Path("id") rentId: Int): Response<Rent>

    @PUT("/api/offers/{id}/cancel")
    suspend fun cancelOffer(@Path("id") offerId: Int): Response<NewPropertyApiResponse<String>>

    @POST("/api/offers/{id}/promotion")
    suspend fun promoteOffer(@Path("id") offerId: Int): Response<NewPropertyApiResponse<String>>
}