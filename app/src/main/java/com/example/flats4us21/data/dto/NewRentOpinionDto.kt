package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class NewRentOpinionDto(
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("service")
    val service: Int,
    @SerializedName("location")
    val location: Int,
    @SerializedName("equipment")
    val equipment: Int,
    @SerializedName("qualityForMoney")
    val qualityForMoney: Int,
    @SerializedName("description")
    val description: String
)