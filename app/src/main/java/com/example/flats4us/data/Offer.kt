package com.example.flats4us.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Offer(
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("rentPropositionToShow")
    val rentPropositionToShow: Int?,
    @SerializedName("rentId")
    val rentId: Int?,
    @SerializedName("isInterest")
    val isInterest: Boolean,
    @SerializedName("date")
    val dateIssue: String,
    @SerializedName("offerStatus")
    val status: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("deposit")
    val deposit: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("startDate")
    val startDate: LocalDate,
    @SerializedName("endDate")
    val endDate: LocalDate,
    @SerializedName("numberOfInterested")
    val interestedPeople: Int,
    @SerializedName("regulations")
    val userRegulation: String?,
    @SerializedName("isPromoted")
    val isPromoted: Boolean,
    @SerializedName("property")
    val property: Property,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("surveyOwnerOffer")
    val surveyOwnerOffer: SurveyOwnerOffer
)