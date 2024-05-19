package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class House(
    override val propertyId: Int,
    override val voivodeship: String,
    override val district: String,
    override val street: String,
    override val buildingNumber: String,
    override val city: String,
    override val postalCode: String,
    override val geoLat: Double,
    override val geoLon: Double,
    override val area: Int,
    override val maxNumberOfInhabitants: Int,
    override val constructionYear: Int,
    override val images: MutableList<Image>,
    override val verificationStatus: Int,
    override val numberOfRooms: Int,
    override val equipment: MutableList<Equipment>,
    @SerializedName("landArea")
    val landArea: Int,
    @SerializedName("numberOfFloors")
    val numberOfFloors: Int
) : Property(
    propertyId,
    voivodeship,
    district,
    street,
    buildingNumber,
    city,
    postalCode,
    geoLat,
    geoLon,
    area,
    maxNumberOfInhabitants,
    constructionYear,
    images,
    verificationStatus,
    numberOfRooms,
    equipment
)