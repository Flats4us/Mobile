package com.example.flats4us21.data

import android.os.Parcel
import android.os.Parcelable

data class Offer(
    val dateIssue: String,
    val status: String,
    val price: String,
    val description: String,
    val rentalPeriod: String,
    val interestedPeople: Int,
    val property: Property
) : Parcelable {
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        dateIssue = parcel.readString()!!,
        status = parcel.readString()!!,
        price = parcel.readString()!!,
        description = parcel.readString()!!,
        rentalPeriod = parcel.readString()!!,
        interestedPeople = parcel.readInt(),
        property = parcel.readParcelable(Property::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateIssue)
        parcel.writeString(status)
        parcel.writeString(price)
        parcel.writeString(description)
        parcel.writeString(rentalPeriod)
        parcel.writeInt(interestedPeople)
        parcel.writeParcelable(property, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offer> {
        override fun createFromParcel(parcel: Parcel): Offer {
            return Offer(parcel)
        }

        override fun newArray(size: Int): Array<Offer?> {
            return arrayOfNulls(size)
        }
    }
}
