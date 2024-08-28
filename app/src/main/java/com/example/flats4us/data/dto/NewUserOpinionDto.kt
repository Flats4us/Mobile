package com.example.flats4us.data.dto


import com.google.gson.annotations.SerializedName

data class NewUserOpinionDto(
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("helpful")
    val helpful: Boolean,
    @SerializedName("cooperative")
    val cooperative: Boolean,
    @SerializedName("tidy")
    val tidy: Boolean,
    @SerializedName("friendly")
    val friendly: Boolean,
    @SerializedName("respectingPrivacy")
    val respectingPrivacy: Boolean,
    @SerializedName("communicative")
    val communicative: Boolean,
    @SerializedName("unfair")
    val unfair: Boolean,
    @SerializedName("lackOfHygiene")
    val lackOfHygiene: Boolean,
    @SerializedName("untidy")
    val untidy: Boolean,
    @SerializedName("conflicting")
    val conflicting: Boolean,
    @SerializedName("noisy")
    val noisy: Boolean,
    @SerializedName("notFollowingTheArrangements")
    val notFollowingTheArrangements: Boolean,
    @SerializedName("description")
    val description: String
)