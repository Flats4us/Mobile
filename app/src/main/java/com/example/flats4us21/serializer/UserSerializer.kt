package com.example.flats4us21.serializer

import com.example.flats4us21.data.dto.NewUserDto
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class UserSerializer : JsonSerializer<NewUserDto> {
    override fun serialize(
        src: NewUserDto,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("userType", src.userType.toString())
        jsonObject.addProperty("profilePicture", src.profilePicture.toString())
        jsonObject.addProperty("name", src.name)
        jsonObject.addProperty("surname", src.surname)
        jsonObject.addProperty("address", src.address)
        jsonObject.addProperty("phoneNumber", src.phoneNumber)
        jsonObject.add("links", context?.serialize(src.links))
        jsonObject.add("interests", context?.serialize(src.interest))
        jsonObject.addProperty("birthDate", src.birthDate.toString())
        jsonObject.addProperty("university", src.university)
        jsonObject.addProperty("studentNumber", src.studentNumber)
        jsonObject.addProperty("bankAccount", src.bankAccount)
        jsonObject.addProperty("documentNumber", src.documentNumber)
        jsonObject.addProperty("documentExpireDate", src.documentExpireDate.toString())
        jsonObject.add("document", context?.serialize(src.document))
        jsonObject.addProperty("email", src.email)
        jsonObject.addProperty("password", src.password)
        jsonObject.addProperty("repeatPassword", src.repeatPassword)
        return jsonObject
    }
}