package com.example.flats4us21.services

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiPlaceDataSource {
    private var BASE_URL = "http://www.overpass-api.de"

    fun getVoivodeships(): List<String> {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val query : String = "[out:json];area[\"ISO3166-1\"=\"PL\"];relation[admin_level=4](area);out tags;"

        val retrofitData = retrofitBuilder.create(PlaceService::class.java)

        retrofitData.getVoivodeships(query).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    Log.d("ApiPlaceDataSource", response.body().toString())
                } else {
                    Log.d("ApiPlaceDataSource", response.code().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("ApiPlaceDataSource", t.message.toString())
            }

        })
        return emptyList()
    }
}