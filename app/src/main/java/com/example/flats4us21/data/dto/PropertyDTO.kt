package com.example.flats4us21.data.dto

import android.net.Uri
import com.example.flats4us21.data.Address

data class PropertyDTO(
    val ownerId: Int,
    val address: Address,
    val area: Int,
    val maxResidents: Int,
    val constructionYear: Int,
    val numberOfRooms: Int,
    val equipment: MutableList<String>,
    val image: MutableList<Uri>
)
