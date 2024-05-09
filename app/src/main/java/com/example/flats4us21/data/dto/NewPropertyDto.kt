package com.example.flats4us21.data.dto

import com.google.gson.annotations.SerializedName

data class NewPropertyDto (
    @SerializedName("propertyType")
    val propertyType: Int,
    @SerializedName("province")
    val voivodeship: String,
    @SerializedName("district")
    val district: String?,
    @SerializedName("street")
    val street: String,
    @SerializedName("number")
    val buildingNumber: String,
    @SerializedName("flat")
    val flatNumber: String?,
    @SerializedName("city")
    val city: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("area")
    val area: Int,
    @SerializedName("maxNumberOfInhabitants")
    val maxNumberOfInhabitants: Int,
    @SerializedName("constructionYear")
    val constructionYear: Int,
    @SerializedName("numberOfRooms")
    val numberOfRooms: Int?,
    @SerializedName("floor")
    val floor: Int?,
    @SerializedName("numberOfFloors")
    val numberOfFloors: Int?,
    @SerializedName("plotArea")
    val plotArea: Int?,
    @SerializedName("equipmentIds")
    val equipment: MutableList<Int>
)