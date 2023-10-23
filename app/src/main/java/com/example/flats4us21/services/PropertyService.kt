package com.example.flats4us21.services

import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.data.dto.NewPropertyDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PropertyService {
    @POST("/s22677/JSON-data-example/main/Property/Property.json")
    suspend fun createProperty(@Body newProperty : NewPropertyDto): Response<Void>

    @GET("/s22677/JSON-data-example/main/Property/Property.json")
    suspend fun getProperties() : Call<List<Property>>
}