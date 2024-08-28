package com.example.flats4us.data

enum class PaymentType(val displayName: String) {
    CZYNSZ("Czynsz"),
    DEPOZYT("Kaucja");

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.ordinal == value }
    }
}
