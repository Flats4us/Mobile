package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OfferService {
    @GET("/s22677/JSON-data-example/main/Offer/Offer.json")
    suspend fun getOffers() : List<Offer>

    @GET("/s22677/JSON-data-example/main/Offer/{id}/Offer.json")
    suspend fun getOffer(@Path("id") offerId: Int): Offer

    @POST("/s22677/JSON-data-example/main/Offer/Offer.json")
    suspend fun createOffer(@Body newOffer : NewOfferDto): Response<Void>

}