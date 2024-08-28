package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

open class User(
    @SerializedName("user_Id")
    open val id: Int,
    @SerializedName("user_name")
    open val name: String,
    @SerializedName("user_surname")
    open val surname: String,
    @SerializedName("user_email")
    open val email: String,
    @SerializedName("user_phoneNumber")
    open val phoneNumber: String,
)
