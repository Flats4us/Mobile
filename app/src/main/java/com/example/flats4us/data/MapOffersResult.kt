package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class MapOffersResult(
    @SerializedName("totalCount")
    val totalCount : Int,
    @SerializedName("result")
    val result: List<MapOffer>
)
