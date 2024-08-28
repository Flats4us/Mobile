package com.example.flats4us.data

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

data class Place(val id: Int, val name: String, val voivodeship: String)

object CSVUtils {
    fun loadCities(context: Context, fileName: String): List<Place> {
        val cities = mutableListOf<Place>()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(";")
                val city = Place(tokens[0].toInt(), tokens[1], tokens[2])
                cities.add(city)
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cities
    }
}
