package com.example.flats4us21.services

import com.example.flats4us21.data.Interest
import retrofit2.http.GET

interface InterestService {

    @GET("/s22677/JSON-data-example/main/Interest/Interest.json")
    suspend fun getInterests(): List<Interest>
}
