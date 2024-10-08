package com.example.flats4us.services

import com.example.flats4us.data.Interest
import retrofit2.Response
import retrofit2.http.GET

interface InterestService {

    @GET("/api/interests")
    suspend fun getInterests(): Response<List<Interest>>
}
