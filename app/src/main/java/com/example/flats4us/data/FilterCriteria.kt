package com.example.flats4us.data

data class FilterCriteria(
    val location: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minSize: Double? = null,
    val maxSize: Double? = null,
    val rooms: Int? = null,
    val floor: Int? = null,
    val minFloor: Int? = null,
    val maxFloor: Int? = null,
    val minResidents: Int? = null,
    val maxResidents: Int? = null
)