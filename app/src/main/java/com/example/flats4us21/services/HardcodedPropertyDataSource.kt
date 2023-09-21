package com.example.flats4us21.services

import com.example.flats4us21.R
import com.example.flats4us21.data.Property

class HardcodedPropertyDataSource : PropertyDataSource {
    private val properties : MutableList<Property> = mutableListOf()

    init {

    }

    override fun getUserProperties(): List<Property> {
        return properties
    }
}