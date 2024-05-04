package com.example.flats4us21.data.dto

data class OfferFilter(
    val sorting: String?,
    val pageNumber: Int,
    val pageSize: Int,
    val province: String?,
    val city: String?,
    val distnace: Int?,
    val propertyType: Int?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val district: String?,
    val minArea: Int?,
    val maxArea: Int?,
    val minYear: Int?,
    val maxYear: Int?,
    val minNumberOfRooms: Int?,
    val floor: Int?,
    val equipment: List<Int>?
)
