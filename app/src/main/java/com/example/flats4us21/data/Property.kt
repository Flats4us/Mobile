package com.example.flats4us21.data

import androidx.annotation.DrawableRes

data class Property(
    val city : String,
    val street : String,
    val maxResidents : Int,
    val equipment : String,
    val area : Int,
    val image : List<Int>,
    val numberOfRooms : Int
)
