package com.example.flats4us21.services

import com.example.flats4us21.data.Interest
import retrofit2.http.GET

interface InterestService {

    @GET("/api/interests")
    suspend fun getInterests(): List<Interest>
}
