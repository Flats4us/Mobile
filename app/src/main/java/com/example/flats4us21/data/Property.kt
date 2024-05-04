package com.example.flats4us21.data

import android.net.Uri

open class Property(
    open val id: Int,
    open val owner: Owner,
    open val address: Address,
    open val area: Int,
    open val landArea: Int?,
    open val maxResidents: Int,
    open val constructionYear: Int,
    open val numberOfRooms: Int,
    open val equipment: MutableList<String>,
    open val image: MutableList<String>
)
