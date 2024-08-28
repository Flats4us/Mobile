package com.example.flats4us.services

import com.example.flats4us.data.Equipment
import retrofit2.Response
import retrofit2.http.GET

interface EquipmentService {
    @GET("/api/equipment")
    suspend fun getEquipments(): Response<List<Equipment>>
}
