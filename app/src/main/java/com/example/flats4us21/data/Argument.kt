package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class Argument(
    @SerializedName("argumentId")
    val argumentId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("ownerAcceptanceDate")
    val ownerAcceptanceDate: String?,
    @SerializedName("studentAccceptanceDate")
    val studentAccceptanceDate: String?,
    @SerializedName("argumentStatus")
    val argumentStatus: Int,
    @SerializedName("interventionNeed")
    val interventionNeed: Boolean,
    @SerializedName("interventionNeedDate")
    val interventionNeedDate: Any,
    @SerializedName("argumentCreatedByUserId")
    val argumentCreatedByUserId: Int,
    @SerializedName("groupChatId")
    val groupChatId: Int,
    @SerializedName("student")
    val student: UserShortData,
    @SerializedName("owner")
    val owner: OwnerForArgument,
    @SerializedName("argumentInterventions")
    val argumentInterventions: List<Any>,
    @SerializedName("rent")
    val rent: RentForArgument
)