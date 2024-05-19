package com.example.flats4us21.data.dto

import com.google.gson.annotations.SerializedName

data class NewRentProposition(
    @SerializedName("roommatesEmails")
    val roommatesEmails: MutableList<String>,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("duration")
    val duration: Int
)
