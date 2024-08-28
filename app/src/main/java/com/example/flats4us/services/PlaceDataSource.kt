package com.example.flats4us.services

interface PlaceDataSource {
    fun getVoivodeships(): List<String>
    fun getDistricts(city: String): MutableList<String>
}