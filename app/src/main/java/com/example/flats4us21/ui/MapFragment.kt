package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.flats4us21.data.RentalPlace
import com.example.flats4us21.databinding.ActivityMapBinding
import com.example.flats4us21.viewmodels.MapViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MapFragment : Fragment() {

    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        Configuration.getInstance().load(context,
            context?.let { PreferenceManager.getDefaultSharedPreferences(it) })

        binding.mapFragment.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapFragment.setBuiltInZoomControls(true)
        binding.mapFragment.setMultiTouchControls(true)

        lifecycleScope.launch {
            val rentalData = viewModel.loadData()
            showAvailablePlacesForRent(rentalData)
        }

        binding.searchButton.setOnClickListener {
            val address = binding.addressEditText.text.toString()
            lifecycleScope.launch {
                val geoPoint = addressToGeoPoint(address)
                if (geoPoint != null) {
                    binding.mapFragment.controller.setCenter(geoPoint)
                    binding.mapFragment.controller.setZoom(12.0)
                } else {
                    Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun showAvailablePlacesForRent(rentals: List<RentalPlace>) {
        lifecycleScope.launch {
            for (rental in rentals) {
                val location = addressToGeoPoint(rental.address)
                if (location != null) {
                    addRentalMarker(rental.name, location)
                } else {
                    Toast.makeText(context, "Address not found for ${rental.name}", Toast.LENGTH_SHORT).show()
                }
            }
            if (rentals.isNotEmpty()) {
                val firstLocation = addressToGeoPoint(rentals[0].address)
                if (firstLocation != null) {
                    binding.mapFragment.controller.setCenter(firstLocation)
                    binding.mapFragment.controller.setZoom(12.0)
                }
            }
        }
    }

    private fun addRentalMarker(name: String, location: GeoPoint) {
        val marker = Marker(binding.mapFragment)
        marker.position = location
        marker.title = name
        binding.mapFragment.overlays.add(marker)
        binding.mapFragment.invalidate()
    }

    private suspend fun addressToGeoPoint(address: String): GeoPoint? = withContext(Dispatchers.IO) {
        val baseUrl = "https://nominatim.openstreetmap.org/search"
        val format = "json"
        val url = "$baseUrl?q=${address.replace(" ", "+")}&format=$format&limit=1"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Flats4UsApp")
        val response = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
        val jsonResponse = JSONArray(response)
        if (jsonResponse.length() == 0) {
            return@withContext null
        }
        val firstResult = jsonResponse.getJSONObject(0)
        val lat = firstResult.getDouble("lat")
        val lon = firstResult.getDouble("lon")
        return@withContext GeoPoint(lat, lon)
    }
}
