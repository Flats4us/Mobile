package com.example.flats4us.deserializer

import com.example.flats4us.data.Rent
import com.example.flats4us.data.utils.RentResult
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RentResultDeserializer : JsonDeserializer<RentResult> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RentResult {
        val jsonObject = json.asJsonObject

        val totalCount = jsonObject.get("totalCount").asInt


        val listType = object : TypeToken<List<Rent>>() {}.type

        val result = context.deserialize<List<Rent>>(jsonObject.get("result"), listType)

        return RentResult(totalCount, result)
    }
}

