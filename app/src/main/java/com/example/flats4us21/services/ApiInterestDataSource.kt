package com.example.flats4us21.services

import com.example.flats4us21.URL
import com.example.flats4us21.data.Interest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInterestDataSource : InterestDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val api: InterestService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(InterestService::class.java)
    }

    override suspend fun getInterests(): List<Interest> {
        return api.getInterests()
    }

}
