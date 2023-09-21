package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Property
import com.example.flats4us21.services.HardcodedPropertyDataSource
import com.example.flats4us21.services.PropertyDataSource

class PropertyViewModel: ViewModel() {
    private val propertyRepository : PropertyDataSource = HardcodedPropertyDataSource()

    fun getUserProperties(): List<Property>{
        return propertyRepository.getUserProperties()
    }

}