package com.example.flats4us21.data

import android.content.Context
import com.example.flats4us21.R

enum class PropertyType(val value: Int) {
    FLAT(0),
    HOUSE(1),
    ROOM(2);

    fun toLocalizedString(context: Context): String {
        return when (this) {
            PropertyType.FLAT -> context.getString(R.string.flat)
            PropertyType.HOUSE -> context.getString(R.string.house)
            PropertyType.ROOM -> context.getString(R.string.room)
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun fromValue(value: Int): PropertyType {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("No enum constant with value $value")
        }

        fun fromLocalizedString(context: Context, localizedString: String): PropertyType {
            return when (localizedString) {
                context.getString(R.string.flat) -> FLAT
                context.getString(R.string.house) -> HOUSE
                context.getString(R.string.room) -> ROOM
                else -> throw IllegalArgumentException("No enum constant with localized name $localizedString")
            }
        }
    }
}
