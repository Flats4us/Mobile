package com.example.flats4us.services

import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.Property
import com.example.flats4us.data.dto.NewPropertyDto
import com.example.flats4us.data.dto.NewRentOpinionDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PropertyService {
    @POST("/api/properties")
    suspend fun createProperty(@Body newProperty : NewPropertyDto) : Response<NewPropertyApiResponse<Int>>

    @GET("/api/properties")
    suspend fun getProperties(@Query("showOnlyVerified") showOnlyVerified: Boolean) : Response<List<Property>>

    @GET("/api/properties/{id}")
    suspend fun getProperty(@Path("id") propertyId: Int) : Response<Property>

    @DELETE("/api/properties/{id}")
    suspend fun deleteProperty(@Path("id") propertyId: Int) : Response<Void>

    @PUT("/api/properties/{id}")
    suspend fun updateProperty(
        @Path("id") propertyId: Int,
        @Body newProperty: NewPropertyDto
    ) : Response<NewPropertyApiResponse<String>>

    @Multipart
    @POST("/api/properties/{id}/files")
    suspend fun addFilesToProperty(
        @Path("id") propertyId: Int,
        @Part titleDeed: MultipartBody.Part?,
        @Part images: List<MultipartBody.Part>
    ) : Response<NewPropertyApiResponse<String>>

    @DELETE("/api/properties/{id}/files/{fileId}")
    suspend fun deleteFile(@Path("id") propertyId: Int, @Path("fileId") fileId: String): Response<NewPropertyApiResponse<String>>

    @POST("/api/offers/{rentId}/rent/opinion")
    suspend fun addOpinion(@Path("rentId") rentId: Int, @Body opinion: NewRentOpinionDto) : Response<NewPropertyApiResponse<String>>
}