package com.example.flats4us21.services

import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.NewRentOpinionDto
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

    @Multipart
    @PUT("/api/properties/{id}")
    suspend fun updateProperty(
        @Path("id") propertyId: Int,
        @Part property: MultipartBody.Part,
        @Part provincePart: MultipartBody.Part,
        @Part districtPart: MultipartBody.Part,
        @Part streetPart: MultipartBody.Part,
        @Part numberPart: MultipartBody.Part,
        @Part flatPart: MultipartBody.Part,
        @Part cityPart: MultipartBody.Part,
        @Part postalCodePart: MultipartBody.Part,
        @Part areaPart: MultipartBody.Part,
        @Part maxNumberOfInhabitantsPart: MultipartBody.Part,
        @Part constructionYearPart: MultipartBody.Part,
        @Part numberOfRoomsPart: MultipartBody.Part,
        @Part floorPart: MultipartBody.Part,
        @Part numberOfFloorsPart: MultipartBody.Part,
        @Part plotAreaPart: MultipartBody.Part,
        @Part equipmentParts: List<MultipartBody.Part>
    ) : Response<NewPropertyApiResponse<String>>

    @Multipart
    @POST("/api/properties/{id}/files")
    suspend fun addFilesToProperty(
        @Path("id") propertyId: Int,
        @Part titleDeed: MultipartBody.Part,
        @Part images: List<MultipartBody.Part>
    ) : Response<NewPropertyApiResponse<String>>

    @DELETE("/api/properties/{id}/files/{fileId}")
    suspend fun deleteFile( @Path("fileId") fileId: String)

    @POST("/api/offers/{rentId}/rent/opinion")
    suspend fun addOpinion(@Path("rentId") rentId: Int, @Body opinion: NewRentOpinionDto) : Response<NewPropertyApiResponse<String>>
}