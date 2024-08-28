package com.example.flats4us.data.dto

import com.example.flats4us.data.Address

data class PropertyDTO(
    val ownerId: Int,
    val address: Address,
    val area: Int,
    val maxResidents: Int,
    val constructionYear: Int,
    val numberOfRooms: Int,
    val equipment: MutableList<String>,
    val image: MutableList<String>
)
