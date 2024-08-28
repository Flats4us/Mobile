package com.example.flats4us.data


import com.google.gson.annotations.SerializedName

data class RentForArgument(
    @SerializedName("rentId")
    val rentId: Int,
    @SerializedName("propertyAddress")
    val propertyAddress: String
)