package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.MapFragment
import org.json.JSONArray
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {

    fun loadData(): List<MapFragment.RentalPlace> {
        val jsonData = """
            [
                {"name": "Apartament w Warszawie", "lat": 52.2297, "lon": 21.0122},
                {"name": "Dom w Krakowie", "lat": 50.0647, "lon": 19.9450},
                {"name": "Kawalerka w Poznaniu", "lat": 52.4064, "lon": 16.9252},
                {"name": "Mieszkanie w Gdańsku", "lat": 54.3520, "lon": 18.6466}
            ]
        """
        val jsonArray = JSONArray(jsonData)
        val rentalList = mutableListOf<MapFragment.RentalPlace>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val lat = jsonObject.getDouble("lat")
            val lon = jsonObject.getDouble("lon")
            rentalList.add(MapFragment.RentalPlace(name, GeoPoint(lat, lon)))
        }

        return rentalList
    }
}