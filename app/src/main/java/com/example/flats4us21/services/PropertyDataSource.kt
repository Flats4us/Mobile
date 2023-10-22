package com.example.flats4us21.services

import com.example.flats4us21.data.Property

interface PropertyDataSource {

    suspend fun getUserProperties(): List<Property>
    suspend fun addProperty(property: Property)

}
