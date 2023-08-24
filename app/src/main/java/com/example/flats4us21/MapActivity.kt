package com.example.flats4us21

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {

    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osm_prefs", MODE_PRIVATE))
        setContentView(R.layout.activity_map)

        map = findViewById(R.id.mapFragment)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        // Add a touch listener to handle user taps
//        map.setOnMapClickListener { point, mapView ->
//            addRentalMarker("New Rental", point)
//        }
//
        showAvailablePlacesForRent()
    }

    private fun showAvailablePlacesForRent() {
        val availableRentals = listOf(
            RentalPlace("Apartment 1", GeoPoint(40.7128, -74.0060)),
            RentalPlace("House 2", GeoPoint(34.0522, -118.2437)),
            RentalPlace("Condo 3", GeoPoint(41.8781, -87.6298))
            // Add more rental places here if needed
        )

        for (rental in availableRentals) {
            addRentalMarker(rental.name, rental.location)
        }

        // Move camera to the first rental location
        if (availableRentals.isNotEmpty()) {
            val firstRental = availableRentals[0]
            map.controller.setCenter(firstRental.location)
            map.controller.setZoom(12.0)
        }
    }

    private fun addRentalMarker(name: String, location: GeoPoint) {
        val marker = Marker(map)
        marker.position = location
        marker.title = name
        map.overlays.add(marker)
        map.invalidate() // Refresh the map to show the new marker
    }

    data class RentalPlace(val name: String, val location: GeoPoint)
}
