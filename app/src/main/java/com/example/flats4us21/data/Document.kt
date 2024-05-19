package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("name")
    val name: String?,
    @SerializedName("path")
    val path: String?
)