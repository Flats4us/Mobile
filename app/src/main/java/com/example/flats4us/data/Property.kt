package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

open class Property(
    @SerializedName("propertyId")
    open val propertyId: Int,
    @SerializedName("province")
    open val voivodeship: String,
    @SerializedName("district")
    open val district: String?,
    @SerializedName("street")
    open val street: String,
    @SerializedName("number")
    open val buildingNumber: String,
    @SerializedName("city")
    open val city: String,
    @SerializedName("postalCode")
    open val postalCode: String,
    @SerializedName("geoLat")
    open val geoLat: Double,
    @SerializedName("geoLon")
    open val geoLon: Double,
    @SerializedName("area")
    open val area: Int,
    @SerializedName("maxNumberOfInhabitants")
    open val maxNumberOfInhabitants: Int,
    @SerializedName("constructionYear")
    open val constructionYear: Int,
    @SerializedName("images")
    open val images: List<Image>,
    @SerializedName("avgRating")
    open val avgRating: Int,
    @SerializedName("avgServiceRating")
    open val avgServiceRating: Int,
    @SerializedName("avgLocationRating")
    open val avgLocationRating: Int,
    @SerializedName("avgEquipmentRating")
    open val avgEquipmentRating: Int,
    @SerializedName("avgQualityForMoneyRating")
    open val avgQualityForMoneyRating: Int,
    @SerializedName("verificationStatus")
    open val verificationStatus: Int,
    @SerializedName("numberOfRooms")
    open val numberOfRooms: Int,
    @SerializedName("offers")
    open val offers: List<PropertyOffer>?,
    @SerializedName("equipment")
    open val equipment: List<Equipment>,
    @SerializedName("rentOpinions")
    open val rentOpinions: List<PropertyOpinion>?
)

