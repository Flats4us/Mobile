package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class Rent(
    @SerializedName("rentId")
    val rentId: Int,
    @SerializedName("propertyId")
    val propertyId: Int,
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("isFinished")
    val isFinished: Boolean,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("propertyAddress")
    val propertyAddress: String,
    @SerializedName("propertyType")
    val propertyType: Int,
    @SerializedName("mainTenantId")
    val mainTenantId: Int,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("propertyImages")
    val propertyImages: List<Image>,
    @SerializedName("tenants")
    val tenants: List<Tenant>,
    @SerializedName("payments")
    val payments: List<Payment>,
    @SerializedName("arguments")
    val arguments: List<Argument>
)