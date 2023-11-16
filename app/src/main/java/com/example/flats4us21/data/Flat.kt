package com.example.flats4us21.data

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Flat(
    override val propertyId: Int,
    override val owner: Owner,
    override val area: Int,
    override val buildingNumber: String,
    override val city: String,
    override val constructionYear: Int,
    override val district: String,
    override val equipment: MutableList<Equipment>,
    override val images: MutableList<Bitmap>,
    override val maxResidents: Int,
    override val numberOfRooms: Int,
    override val street: String,
    override val voivodeship: String,
    @SerializedName("floor")
    val floor: String,
    @SerializedName("flatNumber")
    val flatNumber: String
) : com.example.flats4us21.data.dto.Property(
    propertyId,
    owner,
    area,
    buildingNumber,
    city,
    constructionYear,
    district,
    equipment,
    images,
    maxResidents,
    numberOfRooms,
    street,
    voivodeship
)
