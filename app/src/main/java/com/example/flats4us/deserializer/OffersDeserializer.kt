package com.example.flats4us.deserializer

import android.util.Log
import com.example.flats4us.data.Offer
import com.example.flats4us.data.OffersResult
import com.example.flats4us.data.Owner
import com.example.flats4us.data.Property
import com.example.flats4us.data.SurveyOwnerOffer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "OffersDeserializer"

class OffersDeserializer : JsonDeserializer<OffersResult> {
    private val ownerDeserializer = OwnerDeserializer()
    private val propertyDeserializer = PropertyDeserializer()

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OffersResult {
        val offers = mutableListOf<Offer>()
        val totalCount = json.asJsonObject.get("totalCount").asInt
        val jsonArray = json.asJsonObject.getAsJsonArray("result")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        jsonArray.forEach { element ->
            try {
                val jsonObject = element.asJsonObject
                val offerId = jsonObject.get("offerId").asInt
                val rentPropositionToShow = if (jsonObject.get("rentPropositionToShow").isJsonNull) null else jsonObject.get("rentPropositionToShow").asInt
                val rentId = if (jsonObject.get("rentId").isJsonNull) null else jsonObject.get("rentId").asInt
                val isInterested = jsonObject.get("isInterest").asBoolean
                val dateIssue = jsonObject.get("date").asString
                val status = jsonObject.get("offerStatus").asInt
                val price = jsonObject.get("price").asString
                val deposit = jsonObject.get("deposit").asDouble.toString()
                val description = jsonObject.get("description").asString
                val startDateString = jsonObject.get("startDate").asString.split("T")[0]
                val endDateString = jsonObject.get("endDate").asString.split("T")[0]
                val startDate = LocalDate.parse(startDateString, formatter)
                val endDate = LocalDate.parse(endDateString, formatter)
                val interestedPeople = jsonObject.get("numberOfInterested").asInt
                val userRegulation = jsonObject.get("regulations")?.asString.orEmpty()
                val isPromoted = jsonObject.get("isPromoted").asBoolean
                val property = propertyDeserializer.deserialize(
                    jsonObject.get("property"),
                    Property::class.java,
                    context
                )
                val ownerElement = jsonObject.get("owner").asJsonObject
                //Log.d(TAG, "ownerElement: $ownerElement")
                if (ownerElement.isJsonNull) {
                    throw JsonParseException("owner is missing or null")
                }
                val owner = ownerDeserializer.deserialize(ownerElement, Owner::class.java, context)

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
                offers.add(offer)
            } catch (e: Exception) {
                Log.e(TAG, "Error deserializing offer: ${e.message}")
            }
        }
        return OffersResult(totalCount, offers)
    }
}
