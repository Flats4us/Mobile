package com.example.flats4us21.data.utils

import android.content.Context
import com.example.flats4us21.R

object QuestionTranslator {
    fun translateQuestionName(resourceName: String, context: Context): String {
        val resourceId = context.resources.getIdentifier("question_$resourceName", "string", context.packageName)
        return if (resourceId != 0) {
            context.getString(resourceId)
        } else {
            resourceName
        }
    }
    fun translateAnswerName(resourceName: String, context: Context): String {
        val resourceId = context.resources.getIdentifier("answer_$resourceName", "string", context.packageName)
        return if (resourceId != 0) {
            context.getString(resourceId)
        } else {
            resourceName
        }
    }

    private fun getSortMapping(context: Context): Pair<Map<String, String>, Map<String, String>> {
        val sortToUrlMapping = mapOf(
            context.getString(R.string.sorting_option_1) to context.getString(R.string.url_sorting_option_1),
            context.getString(R.string.sorting_option_2) to context.getString(R.string.url_sorting_option_2),
            context.getString(R.string.sorting_option_3) to context.getString(R.string.url_sorting_option_3),
            context.getString(R.string.sorting_option_4) to context.getString(R.string.url_sorting_option_4),
            context.getString(R.string.sorting_option_5) to context.getString(R.string.url_sorting_option_5),
            context.getString(R.string.sorting_option_6) to context.getString(R.string.url_sorting_option_6)
        )
        val urlToSortMapping = sortToUrlMapping.entries.associate { (key, value) -> value to key }

        return Pair(sortToUrlMapping, urlToSortMapping)
    }

    fun convertToUrl(sortingOption: String, context: Context): String {
        val (sortMapping, _) = getSortMapping(context)
        return sortMapping[sortingOption] ?: throw IllegalArgumentException("Invalid sorting option")
    }

    fun convertToSort(sortingOptionUrl: String, context: Context): String {
        val (_, reverseSortMapping) = getSortMapping(context)
        return reverseSortMapping[sortingOptionUrl] ?: throw IllegalArgumentException("Invalid sorting option URL")
    }

    fun translateInterestName(resourceName: String, context: Context): String {
        val resourceId =
            context.resources.getIdentifier("interest_$resourceName", "string", context.packageName)
        return if (resourceId != 0) {
            context.getString(resourceId)
        } else {
            resourceName
        }
    }
}

