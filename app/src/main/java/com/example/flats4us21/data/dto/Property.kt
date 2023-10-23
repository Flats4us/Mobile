package com.example.flats4us21.data.dto

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("propertyId")
    val propertyId: Int,
    @SerializedName("area")
    val area: Int,
    @SerializedName("buildingNumber")
    val buildingNumber: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("constructionYear")
    val constructionYear: Int,
    @SerializedName("district")
    val district: String,
    @SerializedName("equipment")
    val equipment: List<String>,
    @SerializedName("flatNumber")
    val flatNumber: String,
    @SerializedName("floor")
    val floor: String,
    @SerializedName("images")
    val images: List<Bitmap>,
    @SerializedName("landArea")
    val landArea: Int,
    @SerializedName("maxResidents")
    val maxResidents: Int,
    @SerializedName("numberOfFloors")
    val numberOfFloors: Int,
    @SerializedName("numberOfRooms")
    val numberOfRooms: Int,
    @SerializedName("propertyType")
    val propertyType: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("voivodeship")
    val voivodeship: String
)