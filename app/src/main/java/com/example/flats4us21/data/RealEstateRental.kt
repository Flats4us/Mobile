package com.example.flats4us21.data

import java.time.LocalDate

data class RealEstateRental (
    var rentalDate : LocalDate,
    var rentalPeriod : Int,
    var emails : MutableList<String>
)
