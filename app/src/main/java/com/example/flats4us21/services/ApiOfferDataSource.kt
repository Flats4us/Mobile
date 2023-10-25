package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.deserializer.OfferDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiOfferDataSource : OfferDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Offer::class.java, OfferDeserializer())
        .create()

    private val api: OfferService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OfferService::class.java)
    }

    override suspend fun getOffers(): MutableList<Offer> {
        var result : List<Offer> = mutableListOf()
        api.getOffers()
//            .enqueue(object :
//            Callback<List<Offer>?> {
//            override fun onResponse(
//                call: Call<List<Offer>?>,
//                response: Response<List<Offer>?>
//            ) {
//                if (response.isSuccessful && response.body() != null){
//                    result = response.body()!!
//                } else {
//                    Log.e(TAG, "onResponse: ${response.message()}" )
//                }
//            }
//
//            override fun onFailure(call: Call<List<Offer>?>, t: Throwable) {
//                Log.e(TAG, "onResponse: ${t.message}" )
//            }
//
//        })
        return result as MutableList<Offer>
    }

    override fun getWatchedOffers(): List<Offer> {
        TODO("Not yet implemented")
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

    override fun getLastViewedOffers(): List<Offer> {
        TODO("Not yet implemented")
    }

    override fun addOfferToLastViewed(offer: Offer) {
        TODO("Not yet implemented")
    }
}