package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Equipment

interface EquipmentDataSource {

    fun getHardcodedEquipment(): List<String> {
        //TODO: for hardcoded data
        return listOf()
    }
    suspend fun getEquipment(): ApiResult<List<Equipment>>{
        return ApiResult.Success(listOf())
    }
}