package com.example.flats4us.data

data class Address (
    val voivodeship: String,
    val city: String,
    val district: String?,
    val street: String,
    val buildingNumber: String?,
    val flatNumber: String?
)
