package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class MapOffer(
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("isPromoted")
    val isPromoted: Boolean,
    @SerializedName("property")
    val property: MapProperty,
)
