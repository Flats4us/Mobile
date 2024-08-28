package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

class MapProperty (
    @SerializedName("geoLat")
    val geoLat: Double,
    @SerializedName("geoLon")
    val geoLon: Double,
    @SerializedName("propertyType")
    val propertyType: Int,
    @SerializedName("equipment")
    val equipment: List<Equipment>
)
