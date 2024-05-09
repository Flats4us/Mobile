package com.example.flats4us21.data

enum class PropertyType(val value: Int) {
    FLAT(0),
    HOUSE(1),
    ROOM(2);

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun fromValue(value: Int): PropertyType {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("No enum constant with value $value")
        }
    }
}
