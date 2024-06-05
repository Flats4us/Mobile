package com.example.flats4us21.data

enum class PaymentType(val displayName: String) {
    CZYNSZ("Czynsz"),
    DEPOZYT("Depozyt");

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.ordinal == value }
    }
}
