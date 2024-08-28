package com.example.flats4us.deserializer

import android.util.Log
import com.example.flats4us.data.Equipment
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

private const val TAG = "EquipmentDeserializer"

class EquipmentDeserializer: JsonDeserializer<Equipment> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Equipment {
        return try {
            val jsonObject = json.asJsonObject
            val equipmentId = jsonObject.get("equipmentId").asInt
            val name = jsonObject.get("name").asString
            Equipment(equipmentId, name)
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing equipment: ${e.message}")
            throw JsonParseException("Error deserializing equipment", e)
        }
    }

}
