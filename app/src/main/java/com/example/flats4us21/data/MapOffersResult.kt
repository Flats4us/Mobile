package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class MapOffersResult(
    @SerializedName("totalCount")
    val totalCount : Int,
    @SerializedName("result")
    val result: List<MapOffer>
)
