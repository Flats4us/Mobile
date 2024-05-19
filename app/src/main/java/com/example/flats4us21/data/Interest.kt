package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class Interest (
    @SerializedName("interestId")
    val interestId: Int,
    @SerializedName("name")
    val interestName: String
)
