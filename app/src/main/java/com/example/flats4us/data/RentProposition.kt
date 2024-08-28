package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class RentProposition(
    @SerializedName("rentId")
    val rentId: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("tenants")
    val tenants: List<Tenant>
)
