package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class RentDecision(
    @SerializedName("decision")
    val decision: Boolean,
)
