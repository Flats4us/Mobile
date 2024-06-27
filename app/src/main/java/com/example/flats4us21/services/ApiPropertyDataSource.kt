package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.NewRentOpinionDto
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.example.flats4us21.interceptors.AuthInterceptor
import com.example.flats4us21.serializer.PropertySerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

private const val TAG = "ApiPropertyDataSource"
object ApiPropertyDataSource : PropertyDataSource {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Property::class.java, PropertyDeserializer())
        .registerTypeAdapter(NewPropertyDto::class.java, PropertySerializer())
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: PropertyService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PropertyService::class.java)
    }

    override suspend fun getUserProperties(showOnlyVerified: Boolean): ApiResult<List<Property>> {
        return try {
            val response = api.getProperties(showOnlyVerified)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getProperty(propertyId: Int): ApiResult<Property> {
        return try {
            val response = api.getProperty(propertyId)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Odpowiedź jest pusta")
                }
            } else {
                ApiResult.Error("Nie otrzymano danych: ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Wewnętrzny błąd: ${e.message}")
        }
    }

    override suspend fun addProperty(property: NewPropertyDto): ApiResult<Int> {
        return try {
            val createPropertyResponse = api.createProperty(property)
            if(createPropertyResponse.isSuccessful) {
                val responseBody = createPropertyResponse.body()
                val propertyId = responseBody?.result ?: 0
                ApiResult.Success(propertyId)
            } else {
                ApiResult.Error("Failed to add property: ${createPropertyResponse.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun addFilesToProperty(
        propertyId: Int,
        titleDeedFile: File,
        imageFiles: List<File>
    ): ApiResult<String> {
        return try{
            val titleDeedRequestBody = titleDeedFile.asRequestBody("text/plain".toMediaTypeOrNull())
            val titleDeedPart = MultipartBody.Part.createFormData("titleDeed", titleDeedFile.name, titleDeedRequestBody)

            val imageParts = imageFiles.mapIndexed { _, file ->
                val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("images", file.name, requestBody)
                image
            }
            val response = api.addFilesToProperty(propertyId, titleDeedPart, imageParts)
            if(response.isSuccessful){
                val data = response.body()?.result ?: ""
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to add files to the property: ${response.errorBody()?.string()}")
            }
        } catch(e: Exception){
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun addRentOpinion(rentId: Int, opinion: NewRentOpinionDto): ApiResult<String> {
        return try {
            val response = api.addOpinion(rentId, opinion)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else
                ApiResult.Error( "${response.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred in updating profile: ${e.message}")
        }
    }

    override suspend fun deleteProperty(propertyId: Int): ApiResult<String> {
        return try {
            val response = api.deleteProperty(propertyId)
            if(response.isSuccessful) {
                val data = "Deleted property"
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to delete property: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun updateProperty(propertyId: Int, property: NewPropertyDto): ApiResult<NewPropertyApiResponse<String>> {
        return try {
            val propertyTypePart = MultipartBody.Part.createFormData("propertyType", property.propertyType.toString())
            val provincePart = MultipartBody.Part.createFormData("province", property.voivodeship)
            val districtPart = MultipartBody.Part.createFormData("district", property.district ?: "")
            val streetPart = MultipartBody.Part.createFormData("street", property.street)
            val numberPart = MultipartBody.Part.createFormData("number", property.buildingNumber)
            val flatPart = MultipartBody.Part.createFormData("flat", property.flatNumber ?: "")
            val cityPart = MultipartBody.Part.createFormData("city", property.city)
            val postalCodePart = MultipartBody.Part.createFormData("postalCode", property.postalCode)
            val areaPart = MultipartBody.Part.createFormData("area", property.area.toString())
            val maxNumberOfInhabitantsPart = MultipartBody.Part.createFormData("maxNumberOfInhabitants", property.maxNumberOfInhabitants.toString())
            val constructionYearPart = MultipartBody.Part.createFormData("constructionYear", property.constructionYear.toString())
            val numberOfRoomsPart = MultipartBody.Part.createFormData("numberOfRooms", property.numberOfRooms?.toString() ?: "")
            val floorPart = MultipartBody.Part.createFormData("floor", property.floor?.toString() ?: "")
            val numberOfFloorsPart = MultipartBody.Part.createFormData("numberOfFloors", property.numberOfFloors?.toString() ?: "")
            val plotAreaPart = MultipartBody.Part.createFormData("plotArea", property.plotArea?.toString() ?: "")

            val equipmentParts = property.equipment.map { equipmentId ->
                MultipartBody.Part.createFormData("equipmentIds", equipmentId.toString())
            }

            val response = api.updateProperty(
                propertyId,
                propertyTypePart,
                provincePart,
                districtPart,
                streetPart,
                numberPart,
                flatPart,
                cityPart,
                postalCodePart,
                areaPart,
                maxNumberOfInhabitantsPart,
                constructionYearPart,
                numberOfRoomsPart,
                floorPart,
                numberOfFloorsPart,
                plotAreaPart,
                equipmentParts
            )
            if(response.isSuccessful) {
                val data = response.body()!!
                Log.i(TAG, response.message())
                ApiResult.Success(data)
            } else {
                Log.e(TAG, response.errorBody()?.string() ?: "")
                ApiResult.Error("Failed to update property: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message ?: "")
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

}