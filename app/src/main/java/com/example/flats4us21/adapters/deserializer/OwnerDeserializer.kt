package com.example.flats4us21.deserializer

import com.example.flats4us21.data.Image
import com.example.flats4us21.data.Owner
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val TAG = "OwnerDeserializer"
class OwnerDeserializer : JsonDeserializer<Owner> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Owner {
        val jsonObject = json.asJsonObject

        val userId = jsonObject.get("userId").asInt
        val name = jsonObject.get("name").asString
        val surname = jsonObject.get("surname").asString
        val email = jsonObject.get("email").asString
        val phoneNumber = jsonObject.get("phoneNumber").asString
        val profilePictureElement = jsonObject.get("profilePicture")
        val imageName = if (!profilePictureElement.isJsonNull) profilePictureElement.asJsonObject.get("name").asString else ""
        val imagePath = if (!profilePictureElement.isJsonNull) profilePictureElement.asJsonObject.get("path").asString else ""

        val owner = Owner(userId, name, surname, email, phoneNumber, Image(imageName, imagePath), true)

        //Log.d(TAG, "[deserialize] Owner: $owner")

        return owner
    }
}