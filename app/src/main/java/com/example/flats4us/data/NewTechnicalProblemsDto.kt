package com.example.flats4us.data


import com.google.gson.annotations.SerializedName

data class NewTechnicalProblemsDto(
    @SerializedName("kind")
    val kind: Int,
    @SerializedName("description")
    val description: String
)