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
import com.example.flats4us21.R
import com.example.flats4us21.data.FilterCriteria
import com.example.flats4us21.data.Property
import com.example.flats4us21.databinding.ActivityMapBinding
import com.example.flats4us21.viewmodels.MapViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MapFragment : Fragment(), FilterFragmentListener {

    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private var currentFilter: FilterCriteria? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        Configuration.getInstance().load(
            context,
            context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        )

        binding.mapFragment.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapFragment.setBuiltInZoomControls(true)
        binding.mapFragment.setMultiTouchControls(true)

        lifecycleScope.launch {
            val rentalData = viewModel.loadData(currentFilter)
            showAvailableProperties(rentalData)
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

        binding.filterButton.setOnClickListener {
            binding.filterButton.visibility = View.GONE
            binding.searchButton.visibility = View.GONE

            // TU do poprawy
            val filterFragment = FilterFragment()
            filterFragment.listener = this@MapFragment

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, filterFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    private fun showAvailableProperties(properties: List<Property>) {
        lifecycleScope.launch {
            // Clear previous markers
            binding.mapFragment.overlays.clear()

            for (property in properties) {
                val location = addressToGeoPoint("${property.street} ${property.buildingNumber}, ${property.city}")
                if (location != null) {
                    val description = getPropertyDescription(property)
                    addPropertyMarker(description, location)
                } else {
                    Toast.makeText(context, "Address not found for ${property.city}", Toast.LENGTH_SHORT).show()
                }
            }
            if (properties.isNotEmpty()) {
                val firstLocation = addressToGeoPoint("${properties[0].street} ${properties[0].buildingNumber}, ${properties[0].city}")
                if (firstLocation != null) {
                    binding.mapFragment.controller.setCenter(firstLocation)
                    binding.mapFragment.controller.setZoom(12.0)
                }
            }
            // Refresh the map
            binding.mapFragment.invalidate()
        }
    }


    private fun addPropertyMarker(description: String, location: GeoPoint) {
        val marker = Marker(binding.mapFragment)
        marker.position = location
        marker.title = description
        binding.mapFragment.overlays.add(marker)
        binding.mapFragment.invalidate()
    }

    private fun getPropertyDescription(property: Property): String {
        return """
            Type: ${property.propertyType.name}
            Voivodeship: ${property.voivodeship}
            City: ${property.city}
            District: ${property.district}
            Street: ${property.street} ${property.buildingNumber}
            Area: ${property.area}m²
            Max Residents: ${property.maxResidents}
            Construction Year: ${property.constructionYear}
            Number of Rooms: ${property.numberOfRooms}
            Number of Floors: ${property.numberOfFloors}
            Equipment: ${property.equipment.joinToString(", ")}
        """.trimIndent()
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

    override fun onFilterApplied(filterCriteria: FilterCriteria) {
        currentFilter = filterCriteria
        binding.filterButton.visibility = View.VISIBLE
        binding.searchButton.visibility = View.VISIBLE
        lifecycleScope.launch {
            val rentalData = viewModel.loadData(currentFilter)
            showAvailableProperties(rentalData)
        }
    }
}
