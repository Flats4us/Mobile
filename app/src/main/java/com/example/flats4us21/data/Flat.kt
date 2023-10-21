package com.example.flats4us21.data

import android.net.Uri

data class Flat(
    override val id: Int,
    override val owner: Owner,
    override val address: Address,
    override val area: Int,
    override val maxResidents: Int,
    override val constructionYear: Int,
    override val numberOfRooms: Int,
    override val equipment: MutableList<String>,
    override val image: MutableList<Uri>,
    val floor: Int,
    val flatNumber: Int
) : Property(
    id,
    owner,
    address,
    area,
    maxResidents,
    constructionYear,
    numberOfRooms,
    equipment,
    image
)
