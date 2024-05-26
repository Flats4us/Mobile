package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class RentDecision(
    @SerializedName("decision")
    val decision: Boolean,
)
