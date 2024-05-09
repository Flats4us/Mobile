package com.example.flats4us21.deserializer

import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Owner
import com.example.flats4us21.data.SurveyOwnerOffer
import com.example.flats4us21.data.dto.Property
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "OfferDeserializer"
class OfferDeserializer : JsonDeserializer<Offer> {
    private val ownerDeserializer = OwnerDeserializer()
    private val propertyDeserializer = PropertyDeserializer()

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Offer {
        val jsonObject = json.asJsonObject
        val offerId = jsonObject.get("offerId").asInt
        val dateIssue = jsonObject.get("date").asString
        val status = jsonObject.get("offerStatus").asString
        val price = jsonObject.get("price").asDouble.toString()
        val deposit = jsonObject.get("deposit").asDouble.toString()
        val description = jsonObject.get("description").asString
        val startDateString = jsonObject.get("startDate").asString
        val endDateString = jsonObject.get("endDate").asString
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val startDate = LocalDateTime.parse(startDateString, formatter)
        val endDate = LocalDateTime.parse(endDateString, formatter)
        val interestedPeople = jsonObject.get("numberOfInterested").asInt
        val userRegulation = jsonObject.get("regulations")?.asString.orEmpty()
        val isPromoted = jsonObject.get("isPromoted").asBoolean
        val property : Property = propertyDeserializer.deserialize(jsonObject.get("property"), Property::class.java, context)
        val owner: Owner = ownerDeserializer.deserialize(jsonObject.get("owner"), Owner::class.java, context)
        val surveyOwnerOfferJson = jsonObject.get("surveyOwnerOffer").asJsonObject
        val smokingAllowed = surveyOwnerOfferJson.get("smokingAllowed").asBoolean
        val partiesAllowed = surveyOwnerOfferJson.get("partiesAllowed").asBoolean
        val animalsAllowed = surveyOwnerOfferJson.get("animalsAllowed").asBoolean
        val gender = surveyOwnerOfferJson.get("gender").asInt
        val surveyOwnerOffer = SurveyOwnerOffer(smokingAllowed, partiesAllowed, animalsAllowed, gender)
        val offer = Offer(
            offerId,
            null,
            dateIssue,
            status,
            price,
            deposit,
            description,
            startDate,
            endDate,
            interestedPeople,
            userRegulation,
            isPromoted,
            property,
            owner,
            surveyOwnerOffer
        )
        //Log.d(TAG, "[deserialize] This is my offer: $offer")
        return offer
    }
}
