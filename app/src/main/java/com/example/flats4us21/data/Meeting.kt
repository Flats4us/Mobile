package com.example.flats4us21.data

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime

data class Meeting(
    val date : LocalDateTime,
    val reason : String,
    val student : Student,
    val offer : Offer,
    val status : MeetingStatus
) : Parcelable {
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
    date = parcel.readSerializable() as LocalDateTime,
    reason = parcel.readString()!!,
    student = parcel.readParcelable(Student::class.java.classLoader)!!,
    offer = parcel.readParcelable(Offer::class.java.classLoader)!!,
    status = MeetingStatus.valueOf(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(date)
        parcel.writeString(reason)
        parcel.writeParcelable(student, flags)
        parcel.writeParcelable(offer, flags)
        parcel.writeString(status.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Meeting> {
        override fun createFromParcel(parcel: Parcel): Meeting {
            return Meeting(parcel)
        }

        override fun newArray(size: Int): Array<Meeting?> {
            return arrayOfNulls(size)
        }
    }
}
