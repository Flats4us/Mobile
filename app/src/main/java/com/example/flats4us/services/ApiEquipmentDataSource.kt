package com.example.flats4us.services

import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Equipment
import com.example.flats4us.deserializer.EquipmentDeserializer
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

    override suspend fun getEquipment(): ApiResult<List<Equipment>> {
        return try {
            val response = api.getEquipments()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_equipment")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_equipment")
        }
    }


}