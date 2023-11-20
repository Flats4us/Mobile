package com.example.flats4us21.deserializer

import android.util.Log
import com.example.flats4us21.data.Equipment
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val TAG = "EquipmentDeserializer"

class EquipmentDeserializer: JsonDeserializer<Equipment> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Equipment {
        val jsonObject = json.asJsonObject

        val equipmentId = jsonObject.get("equipmentId").asInt
        val equipmentName = jsonObject.get("name").asString

        val equipment = Equipment(equipmentId, equipmentName)
        Log.d(TAG, "Equipment: $equipment")
        return equipment
    }

}
