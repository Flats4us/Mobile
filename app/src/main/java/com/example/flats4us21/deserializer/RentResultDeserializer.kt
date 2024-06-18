package com.example.flats4us21.deserializer

import com.example.flats4us21.data.Rent
import com.example.flats4us21.data.utils.RentResult
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RentResultDeserializer : JsonDeserializer<RentResult> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RentResult {
        val jsonObject = json.asJsonObject

        val totalCount = jsonObject.get("totalCount").asInt

        // Define the type of the list of Rent objects
        val listType = object : TypeToken<List<Rent>>() {}.type

        val result = context.deserialize<List<Rent>>(jsonObject.get("result"), listType)

        return RentResult(totalCount, result)
    }
}

