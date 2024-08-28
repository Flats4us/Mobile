package com.example.flats4us.data.dto


import com.google.gson.annotations.SerializedName

data class NewArgumentDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rentId")
    val rentId: Int
)