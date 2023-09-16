package com.example.flats4us21.data

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class Student(
    override val name : String,
    override val surname : String,
    override val email : String,
    val birthDate : LocalDate,
    val studentNumber : String,
    val university : String,
    ) : User(name, surname, email), Parcelable {
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        name = parcel.readString()!!,
        surname = parcel.readString()!!,
        email = parcel.readString()!!,
        birthDate = parcel.readSerializable() as LocalDate,
        studentNumber = parcel.readString()!!,
        university = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(email)
        parcel.writeSerializable(birthDate)
        parcel.writeString(studentNumber)
        parcel.writeString(university)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}
