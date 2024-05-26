package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("age")
    val age: Int?,
    @SerializedName("userType")
    val userType: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("accountCreationDate")
    val accountCreationDate: String,
    @SerializedName("verificationStatus")
    val verificationStatus: Int,
    @SerializedName("profilePicture")
    val profilePicture: Image,
    @SerializedName("avgRating")
    val avgRating: Float,
    @SerializedName("sumHelpful")
    val sumHelpful: Int,
    @SerializedName("sumCooperative")
    val sumCooperative: Int,
    @SerializedName("sumTidy")
    val sumTidy: Int,
    @SerializedName("sumFriendly")
    val sumFriendly: Int,
    @SerializedName("sumRespectingPrivacy")
    val sumRespectingPrivacy: Int,
    @SerializedName("sumCommunicative")
    val sumCommunicative: Int,
    @SerializedName("sumUnfair")
    val sumUnfair: Int,
    @SerializedName("sumLackOfHygiene")
    val sumLackOfHygiene: Int,
    @SerializedName("sumUntidy")
    val sumUntidy: Int,
    @SerializedName("sumConflicting")
    val sumConflicting: Int,
    @SerializedName("sumNoisy")
    val sumNoisy: Int,
    @SerializedName("sumNotFollowingTheArrangements")
    val sumNotFollowingTheArrangements: Int,
    @SerializedName("userOpinions")
    val userOpinions: List<String>,
    @SerializedName("university")
    val university: String?,
    @SerializedName("links")
    val links: List<String>?,
    @SerializedName("surveyStudent")
    val surveyStudent: StudentSurvey?,
    @SerializedName("interests")
    val interests: List<Interest>
)