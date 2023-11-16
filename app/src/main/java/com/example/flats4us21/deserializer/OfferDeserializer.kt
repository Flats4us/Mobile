package com.example.flats4us21.deserializer

import android.util.Log
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val TAG = "OfferDeserializer"
class OfferDeserializer : JsonDeserializer<Offer> {
    private val propertyDeserializer = PropertyDeserializer()
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Offer {
        val jsonObject = json.asJsonObject
        val offerId = jsonObject.get("offerId").asInt
        val dateIssue = jsonObject.get("dateIssue").asString
        val status = jsonObject.get("status").asString
        val price = jsonObject.get("price").asDouble.toString()
        val description = jsonObject.get("description").asString
        val rentalPeriod = jsonObject.get("rentalPeriod").asInt.toString()
        val interestedPeople = jsonObject.get("interestedPeople").asInt
        val userRegulation = jsonObject.get("userRegulation")?.asString.orEmpty()
        val property : Property = propertyDeserializer.deserialize(jsonObject.get("property"), Property::class.java, context)

        val offer = Offer(offerId, dateIssue, status, price, description, rentalPeriod, interestedPeople, userRegulation, property)
        Log.d(TAG, "[deserialize] This is my offer: $offer")
        return offer
    }
}