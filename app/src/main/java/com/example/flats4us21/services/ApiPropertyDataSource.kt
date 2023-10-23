package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "ApiPropertyDataSource"

object ApiPropertyDataSource : PropertyDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Property::class.java, PropertyDeserializer())
        .create()

    private val api: PropertyService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PropertyService::class.java)
    }

    override suspend fun getUserProperties(): MutableList<Property> {
        var result : MutableList<Property> = mutableListOf()
        api.getProperties().enqueue(object : Callback<List<Property>?>{
            override fun onResponse(
                call: Call<List<Property>?>,
                response: Response<List<Property>?>
            ) {
                if (response.isSuccessful && response.body() != null){
                    result = (response.body() as MutableList<Property>?)!!
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<List<Property>?>, t: Throwable) {
                Log.e(TAG, "onResponse: ${t.message}" )
            }

        })
        return result
    }

    override suspend fun addProperty(property: Property) {
        TODO("Not yet implemented")
    }

}