package com.example.flats4us21.services

import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiPropertyDataSource"

object ApiPropertyDataSource : PropertyDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Property::class.java, PropertyDeserializer())
        .create()

    private val api: PropertyService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PropertyService::class.java)
    }

    override suspend fun getUserProperties(): List<Property> {
        return api.getProperties()
    }

    override suspend fun addProperty(property: Property) {
        TODO("Not yet implemented")
    }

}