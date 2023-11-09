package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flats4us21.R
import com.example.flats4us21.data.FilterCriteria
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.ActivityMapBinding
import com.example.flats4us21.viewmodels.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import androidx.preference.PreferenceManager
import com.example.flats4us21.data.Property
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.net.HttpURLConnection
import java.net.URL

class MapFragment : Fragment(), FilterFragmentListener {

    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private var currentFilter: FilterCriteria? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val offerData = viewModel.loadOffers(currentFilter)
            showAvailableOffers(offerData)
        }

        binding.searchButton.setOnClickListener {
            val address = binding.addressEditText.text.toString()
            lifecycleScope.launch {
                val geoPoint = addressToGeoPoint(address)
                geoPoint?.let {
                    binding.mapFragment.controller.setCenter(it)
                    binding.mapFragment.controller.setZoom(12.0)
                } ?: Toast.makeText(context, "Address not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.filterButton.setOnClickListener {
            binding.filterButton.visibility = View.GONE
            binding.searchButton.visibility = View.GONE

            val filterFragment = FilterFragment()
            filterFragment.listener = this@MapFragment

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, filterFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    private fun showAvailableOffers(offers: List<Offer>) {
        lifecycleScope.launch {
            binding.mapFragment.overlays.clear()

            for (offer in offers) {
                val property = offer.property
                val location = addressToGeoPoint("${property.street} ${property.buildingNumber}, ${property.city}")
                location?.let {
                    val description = getOfferDescription(offer)
                    addOfferMarker(offer, it)
                } ?: Toast.makeText(context, "Address not found for ${property.city}", Toast.LENGTH_SHORT).show()
            }

            if (offers.isNotEmpty()) {
                offers.first().property.let { property ->
                    addressToGeoPoint("${property.street} ${property.buildingNumber}, ${property.city}")?.let {
                        binding.mapFragment.controller.setCenter(it)
                        binding.mapFragment.controller.setZoom(12.0)
                    }
                }
            }

            binding.mapFragment.invalidate()
        }
    }

    private fun addOfferMarker(offer: Offer, location: GeoPoint) {
        val marker = Marker(binding.mapFragment)
        marker.position = location
        marker.title = offer.property.street // Title for the marker

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // Custom InfoWindow with a button
        val infoWindow = object : InfoWindow(R.layout.info_window_layout, binding.mapFragment) {
            override fun onOpen(item: Any?) {
                val title = mView.findViewById<TextView>(R.id.info_window_title)
                val description = mView.findViewById<TextView>(R.id.info_window_description)
                val button = mView.findViewById<Button>(R.id.info_window_button)

                title.text = offer.property.street
                description.text = getOfferDescription(offer)

                button.setOnClickListener {
                    Log.d("MapFragment", "View Details button clicked for offer: ${offer.description}")
                    viewModel.selectedOffer = offer
                    Log.d("MapFragment", "Selected offer set in ViewModel")

                    // Perform the fragment transaction to show the OfferDetailFragment
                    val offerDetailFragment = OfferDetailFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, offerDetailFragment)
                        .addToBackStack(null)
                        .commit() // Using commit() here, you may switch to commitAllowingStateLoss() if necessary

                    Log.d("MapFragment", "Fragment transaction committed")
                    close() // Close the InfoWindow
                }
            }

            override fun onClose() {
                // Optional: Do something when the InfoWindow is closed if needed.
            }
        }

        // Attach the custom InfoWindow to the marker
        marker.infoWindow = infoWindow

        // Set the listener for marker click
        marker.setOnMarkerClickListener { clickedMarker, mapView ->
            // Toggle the InfoWindow
            if (!clickedMarker.isInfoWindowShown) {
                clickedMarker.showInfoWindow()
            } else {
                clickedMarker.closeInfoWindow()
            }
            true // Return true to indicate we've consumed the event
        }

        binding.mapFragment.overlays.add(marker)
    }






    private fun getOfferDescription(offer: Offer): String {
        return """
            Date Issued: ${offer.dateIssue}
            Status: ${offer.status}
            Price: ${offer.price}
            Description: ${offer.description}
            Rental Period: ${offer.rentalPeriod}
            Interested People: ${offer.interestedPeople}
            Property: ${getPropertyDescription(offer.property)}
        """.trimIndent()
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
            val offerData = viewModel.loadOffers(currentFilter)
            showAvailableOffers(offerData)
        }
    }
}
