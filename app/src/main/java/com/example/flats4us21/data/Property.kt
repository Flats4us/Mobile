package com.example.flats4us21.data

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Property(
    val propertyType: PropertyType,
    val voivodeship: String,
    val city: String,
    val district: String,
    val street: String,
    val buildingNumber: String,
    val area: Int,
    val maxResidents: Int,
    val constructionYear: Int,
    val numberOfRooms: Int,
    val numberOfFloors: Int,
    val equipment: String,
    val image: MutableList<Uri>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        propertyType = PropertyType.valueOf(parcel.readString()!!),
        voivodeship = parcel.readString()!!,
        city = parcel.readString()!!,
        district = parcel.readString()!!,
        street = parcel.readString()!!,
        buildingNumber = parcel.readString()!!,
        area = parcel.readInt(),
        maxResidents = parcel.readInt(),
        constructionYear = parcel.readInt(),
        numberOfRooms = parcel.readInt(),
        numberOfFloors = parcel.readInt(),
        equipment = parcel.readString()!!,
        image = mutableListOf<Uri>().apply { parcel.readList(this, Int::class.java.classLoader) }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(propertyType.name)
        parcel.writeString(voivodeship)
        parcel.writeString(city)
        parcel.writeString(district)
        parcel.writeString(street)
        parcel.writeInt(area)
        parcel.writeInt(maxResidents)
        parcel.writeInt(constructionYear)
        parcel.writeInt(numberOfRooms)
        parcel.writeInt(numberOfFloors)
        parcel.writeString(equipment)
        parcel.writeList(image)
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
