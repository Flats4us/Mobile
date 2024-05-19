package com.example.flats4us21.deserializer

import android.util.Log
import com.example.flats4us21.data.MapOffer
import com.example.flats4us21.data.MapOffersResult
import com.example.flats4us21.data.MapProperty
import com.example.flats4us21.data.OffersResult
import com.example.flats4us21.data.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val TAG = "OffersDeserializer"
class MapOffersDeserializer: JsonDeserializer<MapOffersResult> {
    private val propertyDeserializer = MapPropertyDeserializer()

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MapOffersResult {
        val offers = mutableListOf<MapOffer>()
        val totalCount = json.asJsonObject.get("totalCount").asInt
        val jsonArray = json.asJsonObject.getAsJsonArray("result")

        jsonArray.forEach { element ->
            val jsonObject = element.asJsonObject
            val offerId = jsonObject.get("offerId").asInt
            val isPromoted = jsonObject.get("isPromoted").asBoolean
            val property: MapProperty = propertyDeserializer.deserialize(
                jsonObject.get("property"),
                Property::class.java,
                context
            )
            val mapOffer = MapOffer(offerId, isPromoted, property)
            offers.add(mapOffer)
            Log.d(TAG, "[deserialize] Offer: $mapOffer")
        }
        return MapOffersResult(totalCount, offers)
    }
}