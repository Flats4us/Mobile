package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Offer(
    @SerializedName("offerId")
    val offerId: Int,
    @SerializedName("rentPropositionToShow")
    val rentPropositionToShow: Int?,
    @SerializedName("isInterest")
    val isInterest: Boolean,
    @SerializedName("date")
    val dateIssue: String,
    @SerializedName("offerStatus")
    val status: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("deposit")
    val deposit: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("startDate")
    val startDate: LocalDateTime,
    @SerializedName("endDate")
    val endDate: LocalDateTime,
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