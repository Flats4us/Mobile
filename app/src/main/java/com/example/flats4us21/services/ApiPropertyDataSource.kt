package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.data.Property
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "ApiPropertyDataSource"

object ApiPropertyDataSource : PropertyDataSource {

    private  val baseUrl = "https://raw.githubusercontent.com"

    val api: PropertyService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PropertyService::class.java)
    }

    override suspend fun getUserProperties(): List<Property> {
        var result : List<Property> = mutableListOf()
        api.getProperties().enqueue(object : Callback<List<Property>?>{
            override fun onResponse(
                call: Call<List<Property>?>,
                response: Response<List<Property>?>
            ) {
                if (response.isSuccessful && response.body() != null){
                    result = response.body()!!
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