package com.example.flats4us21.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceDataSource {
    fun getVoivodeships(): List<String>
}