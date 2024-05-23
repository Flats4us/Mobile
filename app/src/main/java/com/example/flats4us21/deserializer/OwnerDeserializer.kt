package com.example.flats4us21.deserializer

import android.util.Log
import com.example.flats4us21.data.Image
import com.example.flats4us21.data.Owner
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

private const val TAG = "OwnerDeserializer"

class OwnerDeserializer : JsonDeserializer<Owner> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Owner {
        try {
            val jsonObject = json.asJsonObject

            val userId = jsonObject.get("userId")?.asInt ?: throw JsonParseException("userId is missing or null")
            val name = jsonObject.get("name")?.asString ?: throw JsonParseException("name is missing or null")
            val surname = jsonObject.get("surname")?.asString ?: throw JsonParseException("surname is missing or null")
            val email = jsonObject.get("email")?.asString ?: throw JsonParseException("email is missing or null")
            val phoneNumber = jsonObject.get("phoneNumber")?.asString ?: throw JsonParseException("phoneNumber is missing or null")

            val profilePictureObject = jsonObject.get("profilePicture").asJsonObject
            val imageName = if (profilePictureObject.get("name").isJsonNull) null else profilePictureObject.get("name").asString
            val imagePath = if (profilePictureObject.get("path").isJsonNull) null else profilePictureObject.get("path").asString

            val profilePicture = if (imageName != null && imagePath != null) Image(imageName, imagePath) else null

            return Owner(userId, name, surname, email, phoneNumber, profilePicture)
        } catch (e: JsonParseException) {
            Log.e(TAG, "Error deserializing owner: ${e.localizedMessage}")
            Log.e(TAG, "Problematic JSON: ${json.toString()}")
            throw JsonParseException("Error deserializing owner", e)
        }
    }
}
