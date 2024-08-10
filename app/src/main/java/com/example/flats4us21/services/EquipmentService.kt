package com.example.flats4us21.services

import com.example.flats4us21.data.Equipment
import retrofit2.Response
import retrofit2.http.GET

interface EquipmentService {
    @GET("/api/equipment")
    suspend fun getEquipments(): Response<List<Equipment>>
}
