package com.example.flats4us21.services

import com.example.flats4us21.data.Property

interface PropertyDataSource {

    fun getUserProperties(): List<Property>

}
