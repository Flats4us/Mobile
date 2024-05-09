package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class SurveyOwnerOffer(
    @SerializedName("smokingAllowed")
    val smokingAllowed: Boolean,
    @SerializedName("partiesAllowed")
    val partiesAllowed: Boolean,
    @SerializedName("animalsAllowed")
    val animalsAllowed: Boolean,
    @SerializedName("gender")
    val gender: Int,
)
