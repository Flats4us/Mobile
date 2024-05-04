package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class RentProposition(
    @SerializedName("roommatesEmails")
    val roommatesEmails: MutableList<String>,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("duration")
    val duration: Int
)
