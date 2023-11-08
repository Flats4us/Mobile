package com.example.flats4us21.serializer

import android.util.Log
import com.example.flats4us21.data.dto.NewOfferDto
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

private const val TAG = "OfferSerializer"
class OfferSerializer : JsonSerializer<NewOfferDto> {
    override fun serialize(
        src: NewOfferDto,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("dateIssue", src.dateIssue)
        jsonObject.addProperty("price", src.price)
        jsonObject.addProperty("description", src.description)
        jsonObject.addProperty("rentalPeriod", src.rentalPeriod)
        jsonObject.addProperty("terms", src.terms)
        jsonObject.addProperty("propertyId", src.propertyId)
        Log.i(TAG, "New offer json object: $jsonObject")

        return jsonObject
    }
}