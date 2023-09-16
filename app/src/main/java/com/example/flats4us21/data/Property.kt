package com.example.flats4us21.data

import android.os.Parcel
import android.os.Parcelable

data class Property(
    val city : String,
    val street : String,
    val maxResidents : Int,
    val equipment : String,
    val area : Int,
    val image : List<Int>,
    val numberOfRooms : Int
) : Parcelable {
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        city = parcel.readString()!!,
        street = parcel.readString()!!,
        maxResidents = parcel.readInt(),
        equipment = parcel.readString()!!,
        area = parcel.readInt(),
        image = mutableListOf<Int>().apply { parcel.readList(this, Int::class.java.classLoader) },
        numberOfRooms = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(street)
        parcel.writeInt(maxResidents)
        parcel.writeString(equipment)
        parcel.writeInt(area)
        parcel.writeList(image)
        parcel.writeInt(numberOfRooms)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Property> {
        override fun createFromParcel(parcel: Parcel): Property {
            return Property(parcel)
        }

        override fun newArray(size: Int): Array<Property?> {
            return arrayOfNulls(size)
        }
    }
}
