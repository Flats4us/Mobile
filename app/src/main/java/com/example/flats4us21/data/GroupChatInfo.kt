package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class GroupChatInfo(
    @SerializedName("groupChatId")
    val groupChatId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("users")
    val users: List<GroupChatUser>
)