package com.example.flats4us21.services

import com.example.flats4us21.data.Equipment
import com.example.flats4us21.deserializer.EquipmentDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiEquipmentDataSource : EquipmentDataSource {

    private const val baseUrl = "http://172.21.40.120:5166"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Equipment::class.java, EquipmentDeserializer())
        .create()

    private val api: EquipmentService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(EquipmentService::class.java)
    }

    override suspend fun getEquipment(): List<Equipment> {
        return api.getEquipments()
    }


}