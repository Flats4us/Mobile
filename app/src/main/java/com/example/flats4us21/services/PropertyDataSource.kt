package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.Property
import java.io.File

interface PropertyDataSource {

    suspend fun getUserProperties(showOnlyVerified: Boolean) : ApiResult<List<Property>>
    suspend fun deleteProperty(propertyId: Int) : ApiResult<String>
    suspend fun updateProperty(propertyId: Int, property: NewPropertyDto) : ApiResult<NewPropertyApiResponse<String>>
    suspend fun addProperty(property: NewPropertyDto): ApiResult<Int>
    suspend fun addFilesToProperty(propertyId: Int, titleDeedFile: File, imageFiles: List<File>) : ApiResult<String>
}
