package com.example.flats4us21.services

import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.example.flats4us21.serializer.PropertySerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiPropertyDataSource"

object ApiPropertyDataSource : PropertyDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Property::class.java, PropertyDeserializer())
        .registerTypeAdapter(Property::class.java, PropertySerializer())
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
        TODO("To delete")
    }

    override suspend fun addProperty(property: NewPropertyDto) {
       api.createProperty(property)
    }

}