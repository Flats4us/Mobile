package com.example.flats4us21.services

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.deserializer.OfferDeserializer
import com.example.flats4us21.serializer.OfferSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiOfferDataSource : OfferDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Offer::class.java, OfferDeserializer())
        .registerTypeAdapter(Offer::class.java, OfferSerializer())
        .create()

    private val api: OfferService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OfferService::class.java)
    }

    override suspend fun getOffers(): List<Offer> {
        return api.getOffers()
    }

    override suspend fun getOffer(offerId: Int): Offer {
        return api.getOffer(offerId)
    }

    override suspend fun getWatchedOffers(): List<Offer> {
        val result : MutableList<Offer> = mutableListOf()
        result.addAll(api.getOffers())
        return result
    }

    override suspend fun addOffer(offer: NewOfferDto) {
        TODO("Not yet implemented")
    }

    override fun addOfferToWatched(offer: Offer) {
        TODO("Not yet implemented")
    }

    override fun removeOfferToWatched(offer: Offer) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastViewedOffers(): List<Offer> {
        val result : MutableList<Offer> = mutableListOf()
        result.addAll(api.getOffers())
        return result
    }

    override fun addOfferToLastViewed(offer: Offer) {
        TODO("Not yet implemented")
    }
}