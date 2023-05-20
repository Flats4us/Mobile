package com.example.flats4us21.deserializer

import com.example.flats4us21.data.SurveyQuestion
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SurveyDeserializer : JsonDeserializer<SurveyQuestion>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SurveyQuestion {
        val jsonObject = json!!.asJsonObject

        val questionId = jsonObject.get("id")?.asInt ?: 0
        val type = jsonObject.get("title")?.asString.orEmpty()
        val content = jsonObject.get("content")?.asString.orEmpty()
        val responseType = jsonObject.get("type_name")?.asString.orEmpty()
        val answers : List<Any?>
        if (responseType == "SUB-QUESTION") {
            val answersJsonArray = jsonObject.get("answers")?.asJsonArray
            answers = answersJsonArray?.map { answerElement ->
                val answerObject = answerElement.asJsonObject
                context?.deserialize(answerObject, SurveyQuestion::class.java)
            }!!
        } else {
            val answersJsonArray = jsonObject.get("answers")?.asJsonArray
            answers = answersJsonArray?.map { answerElement ->
                answerElement.asString
            }!!
        }

        return SurveyQuestion(questionId, type, content, responseType, answers.orEmpty())
    }
}