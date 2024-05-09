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
        jsonObject.addProperty("propertyType", src.propertyType.toString().toInt())
        jsonObject.addProperty("province", src.voivodeship)
        jsonObject.addProperty("district", src.district)
        jsonObject.addProperty("street", src.street)
        jsonObject.addProperty("number", src.buildingNumber)
        jsonObject.addProperty("flat", src.flatNumber)
        jsonObject.addProperty("city", src.city)
        jsonObject.addProperty("postalCode", src.postalCode)
        jsonObject.addProperty("area", src.area)
        jsonObject.addProperty("maxNumberOfInhabitants", src.maxNumberOfInhabitants)
        jsonObject.addProperty("constructionYear", src.constructionYear)
        jsonObject.addProperty("numberOfRooms", src.numberOfRooms)
        jsonObject.addProperty("floor", src.floor)
        jsonObject.addProperty("numberOfFloors", src.numberOfFloors)
        jsonObject.addProperty("plotArea", src.plotArea)
        jsonObject.add("equipmentIds", context?.serialize(src.equipment))
        Log.d(TAG, "[serialize] Json object for new property: $jsonObject")
        return jsonObject
    }
}