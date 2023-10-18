package com.example.flats4us21.services

interface PlaceDataSource {
    fun getVoivodeships(): List<String>
    fun getDistricts(city: String): MutableList<String>
}