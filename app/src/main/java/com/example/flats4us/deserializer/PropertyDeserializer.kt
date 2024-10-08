package com.example.flats4us.deserializer

import android.util.Log
import com.example.flats4us.data.Equipment
import com.example.flats4us.data.Flat
import com.example.flats4us.data.House
import com.example.flats4us.data.Image
import com.example.flats4us.data.Property
import com.example.flats4us.data.PropertyOffer
import com.example.flats4us.data.PropertyOpinion
import com.example.flats4us.data.Room
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

private const val TAG = "PropertyDeserializer"
class PropertyDeserializer : JsonDeserializer<Property> {
    private val equipmentDeserializer = EquipmentDeserializer()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Property {
        return try {
            val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
            val propertyId = jsonObject.get("propertyId").asInt
            val propertyType = jsonObject.get("propertyType").asInt
            val voivodeship = jsonObject.get("province").asString
            val district = if (jsonObject.get("district").isJsonNull) "-" else jsonObject.get("district").asString
            val street = jsonObject.get("street").asString
            val buildingNumber = jsonObject.get("number").asString
            val flatNumber = if (jsonObject.get("flat").isJsonNull) 0 else jsonObject.get("flat").asInt
            val city = jsonObject.get("city").asString
            val postalCode = jsonObject.get("postalCode").asString
            val geoLat = jsonObject.get("geoLat").asDouble
            val geoLon = jsonObject.get("geoLon").asDouble
            val area = jsonObject.get("area").asInt
            val maxNumberOfInhabitants = jsonObject.get("maxNumberOfInhabitants").asInt
            val constructionYear = jsonObject.get("constructionYear").asInt
            val imagesJsonArray = jsonObject.getAsJsonArray("images")
            val images: MutableList<Image> = if (imagesJsonArray != null && !imagesJsonArray.isJsonNull) {
                imagesJsonArray.map { jsonElement ->
                    val imageObject = jsonElement.asJsonObject
                    Image(
                        name = imageObject["name"].asString,
                        path = imageObject["path"].asString
                    )
                }.toMutableList()
            } else {
                mutableListOf()
            }
            val avgRating = jsonObject.get("avgRating").asInt
            val avgServiceRating = jsonObject.get("avgServiceRating").asInt
            val avgLocationRating = jsonObject.get("avgLocationRating").asInt
            val avgEquipmentRating = jsonObject.get("avgEquipmentRating").asInt
            val avgQualityForMoneyRating = jsonObject.get("avgQualityForMoneyRating").asInt

            val verificationStatus = jsonObject.get("verificationStatus").asInt
            val numberOfRooms = if (jsonObject.get("numberOfRooms").isJsonNull) 1 else jsonObject.get("numberOfRooms").asInt
            val numberOfFloors = if (jsonObject.get("numberOfFloors").isJsonNull) 1 else jsonObject.get("numberOfFloors").asInt
            val landArea = if (jsonObject.get("plotArea").isJsonNull) 0 else jsonObject.get("plotArea").asInt
            val floor = if (jsonObject.get("floor").isJsonNull) 0 else jsonObject.get("floor").asInt
            val offersJsonElement = jsonObject["offers"]
            val offers: MutableList<PropertyOffer>? = if (offersJsonElement != null && !offersJsonElement.isJsonNull && offersJsonElement.isJsonArray) {
                val offersJsonArray = offersJsonElement.asJsonArray
                offersJsonArray.map { jsonElement ->
                    val offerObject = jsonElement.asJsonObject
                    val offerId = offerObject["offerId"].asInt
                    val startDate = offerObject["startDate"].asString.split("T")[0]
                    val offerStatus = offerObject["offerStatus"].asInt
                    PropertyOffer(
                        offerId,
                        startDate,
                        offerStatus
                    )
                }.toMutableList()
            } else {
                null
            }

            val equipmentJsonArray = jsonObject.getAsJsonArray("equipment")
            val equipment: MutableList<Equipment> = equipmentJsonArray?.map {
                equipmentDeserializer.deserialize(it, Equipment::class.java, context)
            }?.toMutableList() ?: mutableListOf()
            val propertyOpinionJsonArray = jsonObject.getAsJsonArray("rentOpinions")
            val propertyOpinion : MutableList<PropertyOpinion> = if (propertyOpinionJsonArray != null && !propertyOpinionJsonArray.isJsonNull) {
                propertyOpinionJsonArray.map { jsonElement ->
                    val propertyOpinionObject = jsonElement.asJsonObject
                    val rentOpinionId = propertyOpinionObject["rentOpinionId"].asInt
                    val date = propertyOpinionObject["date"].asString.split("T")[0]
                    val rating = propertyOpinionObject["rating"].asInt
                    val description = propertyOpinionObject["description"].asString
                    val sourceUserId = propertyOpinionObject["sourceUserId"].asInt
                    val sourceUserName = if (propertyOpinionObject["sourceUserName"].isJsonNull) "" else propertyOpinionObject["sourceUserName"].asString
                    val profilePictureObject = if (propertyOpinionObject.has("sourceUserProfilePicture") && !propertyOpinionObject["sourceUserProfilePicture"].isJsonNull) {
                        propertyOpinionObject.getAsJsonObject("sourceUserProfilePicture")
                    } else {
                        null
                    }
                    val imageName = profilePictureObject?.get("name")?.asString
                    val imagePath = profilePictureObject?.get("path")?.asString

                    val profilePicture = if (imageName != null && imagePath != null) Image(imageName, imagePath) else null
                    PropertyOpinion(
                        rentOpinionId,
                        date,
                        rating,
                        description,
                        sourceUserId,
                        sourceUserName,
                        profilePicture
                    )
                }.toMutableList()
            } else {
                mutableListOf()
            }

            when (propertyType) {
                0 -> Flat(propertyId, voivodeship, district, street, buildingNumber, city, postalCode, geoLat, geoLon, area,
                    maxNumberOfInhabitants, constructionYear, images, avgRating, avgServiceRating, avgLocationRating,
                    avgEquipmentRating, avgQualityForMoneyRating, verificationStatus, numberOfRooms, offers, equipment,
                    propertyOpinion, floor, flatNumber)
                1 -> House(propertyId, voivodeship, district, street, buildingNumber, city, postalCode, geoLat, geoLon, area,
                    maxNumberOfInhabitants, constructionYear, images, avgRating, avgServiceRating, avgLocationRating,
                    avgEquipmentRating, avgQualityForMoneyRating, verificationStatus, numberOfRooms, offers, equipment,
                    propertyOpinion, landArea, numberOfFloors)
                else -> Room(propertyId, voivodeship, district, street, buildingNumber, city, postalCode, geoLat, geoLon, area,
                    maxNumberOfInhabitants, constructionYear, images, avgRating, avgServiceRating, avgLocationRating,
                    avgEquipmentRating, avgQualityForMoneyRating, verificationStatus, numberOfRooms, offers, equipment,
                    propertyOpinion, floor, flatNumber)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing property: ${e.message}")
            throw JsonParseException("Error deserializing property", e)
        }
    }
}
