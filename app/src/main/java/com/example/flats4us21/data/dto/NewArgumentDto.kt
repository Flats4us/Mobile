package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class NewArgumentDto(
    @SerializedName("rentId")
    val rentId: Int,
    @SerializedName("description")
    val description: String
)