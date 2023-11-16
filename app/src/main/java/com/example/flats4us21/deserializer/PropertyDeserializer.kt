package com.example.flats4us21.deserializer

import android.graphics.Bitmap
import android.util.Log
import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

private const val TAG = "PropertyDeserializer"
class PropertyDeserializer : JsonDeserializer<Property> {
    private val ownerDeserializer = OwnerDeserializer()
    private val equipmentDeserializer = EquipmentDeserializer()
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Property {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val propertyId = jsonObject.get("propertyId").asInt
        val owner: Owner = ownerDeserializer.deserialize(jsonObject.get("owner"), Owner::class.java, context)
        val area = jsonObject.get("area").asInt
        val buildingNumber = jsonObject.get("buildingNumber").asString
        val city = jsonObject.get("city").asString
        val constructionYear = jsonObject.get("constructionYear").asInt
        val district = if (jsonObject.get("district").isJsonNull) "-" else jsonObject.get("district").asString
        val equipmentJsonArray = jsonObject.getAsJsonArray("equipment")
        val equipment: MutableList<Equipment> = equipmentJsonArray?.map {
            equipmentDeserializer.deserialize(it, Equipment::class.java, context)
        }?.toMutableList()
            ?: mutableListOf()
        val flatNumber =  if (jsonObject.get("flatNumber").isJsonNull) "" else jsonObject.get("flatNumber").asString
        val floor = if (jsonObject.get("floor").isJsonNull) "" else jsonObject.get("floor").asString
        val landArea = if (jsonObject.get("landArea").isJsonNull) 0 else jsonObject.get("landArea").asInt
        val maxResidents = jsonObject.get("maxResidents").asInt
        val numberOfRooms = jsonObject.get("numberOfRooms").asInt
        val propertyType = jsonObject.get("propertyType").asString
        val street = jsonObject.get("street").asString
        val voivodeship = jsonObject.get("voivodeship").asString
        //TODO: correct setting images
        val coilTest  = mutableListOf(defaultBitmap)
        val property : Property = when (propertyType) {
            PropertyType.FLAT.toString() -> {
                Flat(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, coilTest, maxResidents, numberOfRooms, street, voivodeship, floor, flatNumber)
            }
            PropertyType.ROOM.toString() -> {
                Room(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, coilTest, maxResidents, numberOfRooms, street, voivodeship, floor, flatNumber)
            }
            else -> {
                House(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, coilTest, maxResidents, numberOfRooms, street, voivodeship, landArea)
            }
        }
        Log.d(TAG, "[deserialize] This is my property: $property")
        return property
    }

    companion object {
        private lateinit var defaultBitmap : Bitmap

        fun setBitmap(bitmap : Bitmap){
            defaultBitmap = bitmap
        }
    }

}
