package com.example.flats4us21.deserializer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class PropertyDeserializer : JsonDeserializer<Property> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Property {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val propertyId = jsonObject.get("propertyId").asInt
        val owner: Owner = context?.deserialize(jsonObject.get("owner"), Owner::class.java)!!
        val area = jsonObject.get("area").asInt
        val buildingNumber = jsonObject.get("buildingNumber").asString
        val city = jsonObject.get("city").asString
        val constructionYear = jsonObject.get("constructionYear").asInt
        val district = jsonObject.get("district").asString
        val equipment: MutableList<String> = context.deserialize(jsonObject.get("equipment"), List::class.java)!!
        val flatNumber = jsonObject.get("flatNumber").asString
        val floor = jsonObject.get("floor").asString
        val images = deserializeImages(jsonObject.get("images").asJsonArray)
        val landArea = jsonObject.get("landArea").asInt
        val maxResidents = jsonObject.get("maxResidents").asInt
        val numberOfRooms = jsonObject.get("numberOfRooms").asInt
        val propertyType = jsonObject.get("propertyType").asString
        val street = jsonObject.get("street").asString
        val voivodeship = jsonObject.get("voivodeship").asString
        return when (propertyType) {
            PropertyType.FLAT.toString() -> {
                Flat(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, images, maxResidents, numberOfRooms, street, voivodeship, floor, flatNumber)
            }
            PropertyType.ROOM.toString() -> {
                Room(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, images, maxResidents, numberOfRooms, street, voivodeship, floor, flatNumber)
            }
            else -> {
                House(propertyId, owner, area, buildingNumber, city, constructionYear, district, equipment, images, maxResidents, numberOfRooms, street, voivodeship, landArea)
            }
        }
    }

    private fun deserializeImages(jsonArray: JsonElement): MutableList<Bitmap> {
        val decoder: Base64.Decoder = Base64.getDecoder()

        return jsonArray.asJsonArray.map { jsonElement ->
            val base64Image = jsonElement.asString
            val imageBytes = decoder.decode(base64Image)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } as MutableList<Bitmap>
    }
}



