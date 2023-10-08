package com.example.flats4us21

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_map, container, false)

        Configuration.getInstance().load(context,
            context?.let { PreferenceManager.getDefaultSharedPreferences(it) })
        map = view.findViewById(R.id.mapFragment)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val rentalData = loadData()
        showAvailablePlacesForRent(rentalData)

        return view
    }

    private fun loadData(): List<RentalPlace> {
        val jsonData = """
            [
                {"name": "Apartament w Warszawie", "lat": 52.2297, "lon": 21.0122},
                {"name": "Dom w Krakowie", "lat": 50.0647, "lon": 19.9450},
                {"name": "Kawalerka w Poznaniu", "lat": 52.4064, "lon": 16.9252},
                {"name": "Mieszkanie w Gdańsku", "lat": 54.3520, "lon": 18.6466}
            ]
        """
        val jsonArray = JSONArray(jsonData)
        val rentalList = mutableListOf<RentalPlace>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val lat = jsonObject.getDouble("lat")
            val lon = jsonObject.getDouble("lon")
            rentalList.add(RentalPlace(name, GeoPoint(lat, lon)))
        }

        return rentalList
    }

    private fun showAvailablePlacesForRent(rentals: List<RentalPlace>) {
        for (rental in rentals) {
            addRentalMarker(rental.name, rental.location)
        }

        if (rentals.isNotEmpty()) {
            val firstRental = rentals[0]
            map.controller.setCenter(firstRental.location)
            map.controller.setZoom(12.0)
        }
    }

    private fun addRentalMarker(name: String, location: GeoPoint) {
        val marker = Marker(map)
        marker.position = location
        marker.title = name
        map.overlays.add(marker)
        map.invalidate()
    }

    data class RentalPlace(val name: String, val location: GeoPoint)
}
