package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

open class StudentOwner(
    @SerializedName("person_Id")
    override val id: Int,
    @SerializedName("person_name")
    override val name: String,
    @SerializedName("person_surname")
    override val surname: String,
    @SerializedName("person_email")
    override val email: String,
    @SerializedName("person_phoneNumber")
    override val phoneNumber: String,
    @SerializedName("person_profilePicture")
    open val profilePicture: Image?
) : User(id, name, surname, email, phoneNumber)
