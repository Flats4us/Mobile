package com.example.flats4us21.services

import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.Property

interface PropertyDataSource {

    suspend fun getUserProperties(): List<Property>
    suspend fun addProperty(property: Property)
    suspend fun addProperty(property: NewPropertyDto){
        println("Default method: addProperty()")
    }
}
