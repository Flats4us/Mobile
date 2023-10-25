package com.example.flats4us21.deserializer

import android.net.Uri
import com.example.flats4us21.data.Owner
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class OwnerDeserializer : JsonDeserializer<Owner> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Owner {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("ownerId").asInt
        val name = jsonObject.get("ownerName").asString
        val surname = jsonObject.get("ownerSurname").asString
        val email = jsonObject.get("ownerEmail").asString
        val phoneNumber = jsonObject.get("ownerPhoneNumber").asString
        val profilePictureUri = if (jsonObject.get("ownerProfilePicture").isJsonNull) null else Uri.parse(jsonObject.get("ownerProfilePicture").asString)
        val userStatus = jsonObject.get("ownerUserStatus").asString
        val verificationStatus = jsonObject.get("ownerVerificationStatus").asString

        return Owner(id, name, surname, email, phoneNumber, profilePictureUri, userStatus, verificationStatus)
    }
}