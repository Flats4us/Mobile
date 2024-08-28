package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class Flat(
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
    override val avgRating: Int,
    override val avgServiceRating: Int,
    override val avgLocationRating: Int,
    override val avgEquipmentRating: Int,
    override val avgQualityForMoneyRating: Int,
    override val verificationStatus: Int,
    override val numberOfRooms: Int,
    override val offers: List<PropertyOffer>?,
    override val equipment: MutableList<Equipment>,
    override val rentOpinions: List<PropertyOpinion>?,
    @SerializedName("floor")
    val floor: Int,
    @SerializedName("flatNumber")
    val flatNumber: Int
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
    avgRating,
    avgServiceRating,
    avgLocationRating,
    avgEquipmentRating,
    avgQualityForMoneyRating,
    verificationStatus,
    numberOfRooms,
    offers,
    equipment,
    rentOpinions
)
