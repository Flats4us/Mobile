package com.example.flats4us21.data.dto

import com.google.gson.annotations.SerializedName

data class NewOfferDto(
    @SerializedName("dateIssue")
    val dateIssue: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rentalPeriod")
    val rentalPeriod: String,
    @SerializedName("terms")
    val terms: String?,
    @SerializedName("propertyId")
    val propertyId : Int
)
