package com.example.flats4us21.data

import android.os.Parcel
import android.os.Parcelable

data class RentalPlace(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val images: MutableList<Int>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        name = parcel.readString()!!,
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble(),
        description = parcel.readString()!!,
        images = mutableListOf<Int>().apply { parcel.readList(this, Int::class.java.classLoader) }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(description)
        parcel.writeList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RentalPlace> {
        override fun createFromParcel(parcel: Parcel): RentalPlace {
            return RentalPlace(parcel)
        }

        override fun newArray(size: Int): Array<RentalPlace?> {
            return arrayOfNulls(size)
        }
    }
}
