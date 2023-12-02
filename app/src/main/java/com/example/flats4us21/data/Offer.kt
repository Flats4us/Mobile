package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("dateIssue")
    val dateIssue: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rentalPeriod")
    val rentalPeriod: String,
    @SerializedName("interestPeople")
    val interestedPeople: Int,
    @SerializedName("userRegulation")
    val userRegulation: String?,
    @SerializedName("property")
    val property: com.example.flats4us21.data.dto.Property
)