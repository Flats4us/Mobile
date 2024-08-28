package com.example.flats4us.data


import com.google.gson.annotations.SerializedName

data class PropertyOffer(
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("offerStatus")
    val offerStatus: Int
)