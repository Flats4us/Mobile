package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class NewTechnicalProblemsDto(
    @SerializedName("kind")
    val kind: Int,
    @SerializedName("description")
    val description: String
)