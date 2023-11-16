package com.example.flats4us21.data.dto

import android.net.Uri
import com.example.flats4us21.data.Equipment
import com.example.flats4us21.data.PropertyType
import com.google.gson.annotations.SerializedName

data class NewPropertyDto (
    @SerializedName("propertyType")
    val propertyType: PropertyType,
    @SerializedName("voivodeship")
    val voivodeship: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("district")
    val district: String?,
    @SerializedName("street")
    val street: String,
    @SerializedName("buildingNumber")
    val buildingNumber: String?,
    @SerializedName("flatNumber")
    val flatNumber: String?,
    @SerializedName("area")
    val area: Int,
    @SerializedName("landArea")
    val landArea: Int?,
    @SerializedName("maxResidents")
    val maxResidents: Int,
    @SerializedName("constructionYear")
    val constructionYear: Int,
    @SerializedName("elevator")
    val elevator: Boolean,
    @SerializedName("numberOfRooms")
    val numberOfRooms: Int,
    @SerializedName("equipment")
    val equipment: MutableList<Int>,
    @SerializedName("image")
    val image: MutableList<Uri>,
    @SerializedName("ownerId")
    val ownerId: Int
)