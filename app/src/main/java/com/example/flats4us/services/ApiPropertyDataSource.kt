package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.Property
import com.example.flats4us.data.dto.NewPropertyDto
import com.example.flats4us.data.dto.NewRentOpinionDto
import com.example.flats4us.deserializer.PropertyDeserializer
import com.example.flats4us.interceptors.AuthInterceptor
import com.example.flats4us.serializer.PropertySerializer
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_properties")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_properties")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_properties")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_properties")
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
                ApiResult.Error("error_invalid_data")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_property")
        }
    }

    override suspend fun addFilesToProperty(
        propertyId: Int,
        titleDeedFile: File?,
        imageFiles: List<File>
    ): ApiResult<String> {
        return try{
            val titleDeedPart = if (titleDeedFile != null) {
                val mimeType = when (titleDeedFile.extension.lowercase()) {
                    "pdf" -> "application/pdf"
                    "jpg", "jpeg" -> "image/jpeg"
                    "png" -> "image/png"
                    "bmp" -> "image/bmp"
                    else -> "application/octet-stream"
                }

                val titleDeedRequestBody = titleDeedFile.asRequestBody(mimeType.toMediaTypeOrNull())

                MultipartBody.Part.createFormData(
                    "titleDeed",
                    titleDeedFile.name,
                    titleDeedRequestBody
                )
            } else {
                null
            }


            val imageParts = imageFiles.mapIndexed { _, file ->
                val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("images", file.name, requestBody)
                image
            }
            val response = api.addFilesToProperty(propertyId, titleDeedPart, imageParts)
            if(response.isSuccessful){
                val data = response.body()?.result ?: ""
                ApiResult.Success("success_added_files")
            } else {
                ApiResult.Error("error_failed_to_add_files_to_property")
            }
        } catch(e: Exception){
            ApiResult.Error("internal_error_failed_to_add_files_to_property")
        }
    }

    override suspend fun deleteFileFromProperty(
        propertyId: Int,
        fileId: String
    ): ApiResult<String> {
        return try {
            val response = api.deleteFile(propertyId, fileId)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success("success_deleted_files")
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_remove_files_from_property")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_remove_files_from_property")
        }
    }

    override suspend fun addRentOpinion(rentId: Int, opinion: NewRentOpinionDto): ApiResult<String> {
        return try {
            val response = api.addOpinion(rentId, opinion)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success("success_added_rent_opinion")
            } else
                ApiResult.Error( "error_failed_to_add_rent_opinion")
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_rent_opinion")
        }
    }

    override suspend fun deleteProperty(propertyId: Int): ApiResult<String> {
        return try {
            val response = api.deleteProperty(propertyId)
            if(response.isSuccessful) {
                val data = "success_deleted_property"
                ApiResult.Success(data)
            } else {
                ApiResult.Error("error_failed_to_delete_property")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_delete_property")
        }
    }

    override suspend fun updateProperty(propertyId: Int, property: NewPropertyDto): ApiResult<NewPropertyApiResponse<String>> {
        return try {
            val response = api.updateProperty(propertyId, property)
            if(response.isSuccessful) {
                val data = response.body()!!
                Log.i(TAG, response.message())
                ApiResult.Success(NewPropertyApiResponse("success_updated_property"))
            } else {
                Log.e(TAG, response.errorBody()?.string() ?: "")
                ApiResult.Error("error_failed_to_update_property")
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message ?: "")
            ApiResult.Error("internal_error_failed_to_update_property")
        }
    }

}