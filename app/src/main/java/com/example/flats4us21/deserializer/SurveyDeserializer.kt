package com.example.flats4us21.deserializer

import com.example.flats4us21.data.QuestionType
import com.example.flats4us21.data.ResponseType
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
        val typeString = jsonObject.get("title")?.asString.orEmpty()
        val type = when (typeString) {
            "STUDENT" -> QuestionType.STUDENT
            "OWNER" -> QuestionType.OWNER
            else -> QuestionType.OWNER
        }
        val content = jsonObject.get("content")?.asString.orEmpty()
        val responseTypeString = jsonObject.get("type_name")?.asString.orEmpty()
        val responseType = when (responseTypeString) {
            "RADIOBUTTON" -> ResponseType.RADIOBUTTON
            "TEXT" -> ResponseType.TEXT
            "SUB-QUESTION" -> ResponseType.SUBQUESTION
            else -> ResponseType.TEXT
        }
        val answers : List<Any?>
        if (responseType == ResponseType.SUBQUESTION) {
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

        return SurveyQuestion(questionId, type, content, responseType, answers)
    }
}