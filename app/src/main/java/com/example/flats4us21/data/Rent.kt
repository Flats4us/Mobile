package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName


data class Rent(
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("isFinished")
    val isFinished: Boolean,
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("payments")
    val payments: List<Any>,
    @SerializedName("propertyAddress")
    val propertyAddress: String,
    @SerializedName("propertyId")
    val propertyId: Int,
    @SerializedName("propertyImages")
    val propertyImages: List<Image>,
    @SerializedName("propertyType")
    val propertyType: Int,
    @SerializedName("rentId")
    val rentId: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("tenants")
    val tenants: List<Tenant>
)