package com.example.flats4us21.serializer

import android.util.Log
import com.example.flats4us21.data.dto.NewPropertyDto
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

private const val TAG = "PropertySerializer"
class PropertySerializer : JsonSerializer<NewPropertyDto> {
    override fun serialize(
        src: NewPropertyDto,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("propertyType", src.propertyType.toString())
        jsonObject.addProperty("voivodeship", src.voivodeship)
        jsonObject.addProperty("city", src.city)
        jsonObject.addProperty("district", src.district)
        jsonObject.addProperty("street", src.street)
        jsonObject.addProperty("buildingNumber", src.buildingNumber)
        jsonObject.addProperty("flatNumber", src.flatNumber)
        jsonObject.addProperty("area", src.area)
        jsonObject.addProperty("landArea", src.landArea)
        jsonObject.addProperty("maxResidents", src.maxResidents)
        jsonObject.addProperty("constructionYear", src.constructionYear)
        jsonObject.addProperty("elevator", src.elevator)
        jsonObject.addProperty("numberOfRooms", src.numberOfRooms)
        jsonObject.add("equipmentList", context?.serialize(src.equipment))
        jsonObject.add("image", context?.serialize(src.image))
        jsonObject.addProperty("equipment", src.ownerId)
        Log.d(TAG, "Json object for new property: $jsonObject")
        return jsonObject
    }
}