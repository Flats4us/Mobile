package com.example.flats4us.deserializer

import com.example.flats4us.data.Equipment
import com.example.flats4us.data.MapProperty
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class MapPropertyDeserializer : JsonDeserializer<MapProperty> {
    private val equipmentDeserializer = EquipmentDeserializer()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MapProperty {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val geoLat = jsonObject.get("geoLat").asDouble
        val geoLon = jsonObject.get("geoLon").asDouble
        val propertyType = jsonObject.get("propertyType").asInt
        val equipmentJsonArray = jsonObject.getAsJsonArray("equipment")
        val equipment: MutableList<Equipment> = equipmentJsonArray?.map {
            equipmentDeserializer.deserialize(it, Equipment::class.java, context)
        }?.toMutableList()
            ?: mutableListOf()
        val mapProperty = MapProperty(
                geoLat,
                geoLon,
                propertyType,
                equipment
                )
        return mapProperty
    }

}
