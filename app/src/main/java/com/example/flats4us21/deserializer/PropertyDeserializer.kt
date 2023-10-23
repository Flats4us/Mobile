package com.example.flats4us21.deserializer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

        return Property(
            propertyId = jsonObject.get("propertyId").asInt,
            area = jsonObject.get("area").asInt,
            buildingNumber = jsonObject.get("buildingNumber").asString,
            city = jsonObject.get("city").asString,
            constructionYear = jsonObject.get("constructionYear").asInt,
            district = jsonObject.get("district").asString,
            equipment = context?.deserialize(jsonObject.get("equipment"), List::class.java)!!,
            flatNumber = jsonObject.get("flatNumber").asString,
            floor = jsonObject.get("floor").asString,
            images = deserializeImages(jsonObject.get("images").asJsonArray),
            landArea = jsonObject.get("landArea").asInt,
            maxResidents = jsonObject.get("maxResidents").asInt,
            numberOfFloors = jsonObject.get("numberOfFloors").asInt,
            numberOfRooms = jsonObject.get("numberOfRooms").asInt,
            propertyType = jsonObject.get("propertyType").asString,
            street = jsonObject.get("street").asString,
            voivodeship = jsonObject.get("voivodeship").asString
        )
    }

    private fun deserializeImages(jsonArray: JsonElement): List<Bitmap> {
        val decoder: Base64.Decoder = Base64.getDecoder()

        return jsonArray.asJsonArray.map { jsonElement ->
            val base64Image = jsonElement.asString
            val imageBytes = decoder.decode(base64Image)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
    }
}



