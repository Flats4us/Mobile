package com.example.flats4us21.data.utils

import com.example.flats4us21.data.Rent
import com.google.gson.annotations.SerializedName

data class RentResult(
    @SerializedName("totalCount")
    val totalCount : Int,
    @SerializedName("result")
    val result: List<Rent>
)
