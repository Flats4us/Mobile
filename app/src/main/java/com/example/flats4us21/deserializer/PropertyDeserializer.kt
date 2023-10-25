package com.example.flats4us21.deserializer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import java.util.*
import java.util.zip.GZIPInputStream

class PropertyDeserializer : JsonDeserializer<Property> {
    private val ownerDeserializer = OwnerDeserializer()
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
        val district = jsonObject.get("district").asString
        val equipment: MutableList<String> = context?.deserialize(jsonObject.get("equipment"), List::class.java)!!
        val flatNumber = jsonObject.get("flatNumber").asString
        val floor = jsonObject.get("floor").asString
        val images : MutableList<Bitmap> = context.deserialize(jsonObject.get("images"), MutableList::class.java)!!
        val landArea = if (jsonObject.get("landArea").isJsonNull) 0 else jsonObject.get("landArea").asInt
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
        return jsonArray.asJsonArray.mapNotNull { jsonElement ->
            val base64Image = jsonElement.asString
            val decompressedImageBytes = decompressAndBase64ToImage(base64Image)
            Log.d("PropertyDeserializer", "$decompressedImageBytes")
            if (decompressedImageBytes != null) {
                BitmapFactory.decodeByteArray(decompressedImageBytes, 0, decompressedImageBytes.size)
            } else {
                null
            }
        } as MutableList<Bitmap>
    }

    private fun decompressAndBase64ToImage(compressedBase64Image: String): ByteArray? {
        return try {
            val compressedData = Base64.getDecoder().decode(compressedBase64Image)

            val decompressedData = decompressData(compressedData)

            decompressedData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decompressData(compressedData: ByteArray): ByteArray {
        val inputStream = ByteArrayInputStream(compressedData)
        val gzipInputStream = GZIPInputStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)

        var bytesRead: Int
        while (gzipInputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        return outputStream.toByteArray()
    }
}
