package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.Property
import com.example.flats4us.data.dto.NewPropertyDto
import com.example.flats4us.data.dto.NewRentOpinionDto
import java.io.File

interface PropertyDataSource {

    suspend fun getUserProperties(showOnlyVerified: Boolean) : ApiResult<List<Property>>
    suspend fun getProperty(propertyId: Int) : ApiResult<Property>
    suspend fun deleteProperty(propertyId: Int) : ApiResult<String>
    suspend fun updateProperty(propertyId: Int, property: NewPropertyDto) : ApiResult<NewPropertyApiResponse<String>>
    suspend fun addProperty(property: NewPropertyDto): ApiResult<Int>
    suspend fun addFilesToProperty(propertyId: Int, titleDeedFile: File?, imageFiles: List<File>) : ApiResult<String>
    suspend fun deleteFileFromProperty(propertyId: Int, fileId: String) : ApiResult<String>
    suspend fun addRentOpinion(rentId: Int, opinion: NewRentOpinionDto) : ApiResult<String>
}
