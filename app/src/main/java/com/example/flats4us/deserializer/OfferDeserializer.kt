package com.example.flats4us.deserializer

import com.example.flats4us.data.Offer
import com.example.flats4us.data.Owner
import com.example.flats4us.data.Property
import com.example.flats4us.data.SurveyOwnerOffer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
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
        val rentPropositionToShow = if (jsonObject.get("rentPropositionToShow").isJsonNull) null else jsonObject.get("rentPropositionToShow").asInt
        val rentId = if (jsonObject.get("rentId").isJsonNull) null else jsonObject.get("rentId").asInt
        val isInterested = jsonObject.get("isInterest").asBoolean
        val dateIssue = jsonObject.get("date").asString
        val status = jsonObject.get("offerStatus").asInt
        val price = jsonObject.get("price").asString
        val deposit = jsonObject.get("deposit").asString
        val description = jsonObject.get("description").asString
        val startDateString = jsonObject.get("startDate").asString.split("T")[0]
        val endDateString = jsonObject.get("endDate").asString.split("T")[0]
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.parse(startDateString, formatter)
        val endDate = LocalDate.parse(endDateString, formatter)
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
            rentPropositionToShow,
            rentId,
            isInterested,
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
