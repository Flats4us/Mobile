package com.example.flats4us21.services

import com.example.flats4us21.data.Equipment

interface EquipmentDataSource {

    fun getHardcodedEquipment(): List<String> {
        //TODO: for hardcoded data - later to delete
        return listOf()
    }
    suspend fun getEquipment(): List<Equipment>{
        return listOf()
    }
}