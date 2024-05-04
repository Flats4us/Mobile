package com.example.flats4us21.services

import com.example.flats4us21.URL
import com.example.flats4us21.data.Equipment
import com.example.flats4us21.deserializer.EquipmentDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiEquipmentDataSource : EquipmentDataSource {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Equipment::class.java, EquipmentDeserializer())
        .create()

    private val api: EquipmentService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(EquipmentService::class.java)
    }

    override suspend fun getEquipment(): List<Equipment> {
        return api.getEquipments()
    }


}