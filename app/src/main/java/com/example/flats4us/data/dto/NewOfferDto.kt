package com.example.flats4us.data.dto

import com.google.gson.annotations.SerializedName

data class NewOfferDto(
    @SerializedName("propertyId")
    val propertyId : Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("deposit")
    val deposit: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("regulations")
    val regulations: String,
    @SerializedName("smokingAllowed")
    val smokingAllowed: Boolean,
    @SerializedName("partiesAllowed")
    val partiesAllowed: Boolean,
    @SerializedName("animalsAllowed")
    val animalsAllowed: Boolean,
    @SerializedName("gender")
    val gender: Int,
)
