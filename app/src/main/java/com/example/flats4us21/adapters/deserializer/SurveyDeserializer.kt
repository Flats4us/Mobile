package com.example.flats4us21.adapters.deserializer

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
        val type = when (jsonObject.get("title")?.asString.orEmpty()) {
            "STUDENT" -> QuestionType.STUDENT
            "OWNER" -> QuestionType.OWNER
            else -> QuestionType.OWNER
        }
        val content = jsonObject.get("content")?.asString.orEmpty()
        val responseType = when (jsonObject.get("typeName")?.asString.orEmpty()) {
            "RADIOBUTTON" -> ResponseType.RADIOBUTTON
            "TEXT" -> ResponseType.TEXT
            "SUBQUESTION" -> ResponseType.SUBQUESTION
            "SLIDER" -> ResponseType.SLIDER
            "SWITCH" -> ResponseType.SWITCH
            else -> ResponseType.TEXT
        }
        val answers : List<Any?> = when (responseType) {
            ResponseType.SUBQUESTION -> {
                val answersJsonArray = jsonObject.get("answers")?.asJsonArray
                answersJsonArray?.map { answerElement ->
                    val answerObject = answerElement.asJsonObject
                    context?.deserialize(answerObject, SurveyQuestion::class.java)
                }!!
            }
            ResponseType.SLIDER -> {
                val answersJsonArray = jsonObject.get("answers")?.asJsonArray
                answersJsonArray?.map { answerElement ->
                    val answerObject = answerElement.asJsonObject
                    val min = answerObject.get("min").asInt
                    val max = answerObject.get("max").asInt
                    Pair(min, max)
                }!!
            }
            ResponseType.SWITCH -> {
                listOf(false)
            }
            else -> {
                val answersJsonArray = jsonObject.get("answers")?.asJsonArray
                answersJsonArray?.map { answerElement ->
                    answerElement.asString
                }!!
            }
        }

        return SurveyQuestion(questionId, type, content, responseType, answers)
    }
}