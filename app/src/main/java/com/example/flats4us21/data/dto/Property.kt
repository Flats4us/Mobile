package com.example.flats4us21.data.dto

import android.graphics.Bitmap
import com.example.flats4us21.data.Equipment
import com.example.flats4us21.data.Owner
import com.google.gson.annotations.SerializedName

open class Property(
    @SerializedName("propertyId")
    open val propertyId: Int,
    @SerializedName("owner")
    open val owner: Owner,
    @SerializedName("area")
    open val area: Int,
    @SerializedName("buildingNumber")
    open val buildingNumber: String,
    @SerializedName("city")
    open val city: String,
    @SerializedName("constructionYear")
    open val constructionYear: Int,
    @SerializedName("district")
    open val district: String,
    @SerializedName("equipment")
    open val equipment: List<Equipment>,
    @SerializedName("images")
    open val images: List<Bitmap>,
    @SerializedName("maxResidents")
    open val maxResidents: Int,
    @SerializedName("numberOfRooms")
    open val numberOfRooms: Int,
    @SerializedName("street")
    open val street: String,
    @SerializedName("voivodeship")
    open val voivodeship: String
)