package com.example.flats4us21.ui

import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.flats4us21.data.dto.Property
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
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.net.HttpURLConnection
import java.net.URL

class MapFragment : Fragment() {

    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        Configuration.getInstance().load(
            context, PreferenceManager.getDefaultSharedPreferences(context)
        )

        binding.mapFragment.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapFragment.setBuiltInZoomControls(true)
        binding.mapFragment.setMultiTouchControls(true)

        lifecycleScope.launch {
            val offerData = viewModel.loadOffers()
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
            val filterFragment = FilterFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, filterFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun showAvailableOffers(offers: List<Offer>) {
        lifecycleScope.launch {
            binding.mapFragment.overlays.clear()
            offers.forEach { offer ->
                val property = offer.property
                val location = addressToGeoPoint("${property.street} ${property.buildingNumber}, ${property.city}")
                location?.let {
                    addOfferMarker(offer, it)
                } ?: Toast.makeText(context, "Address not found for ${property.city}", Toast.LENGTH_SHORT).show()
            }
            offers.firstOrNull()?.property?.let { property ->
                addressToGeoPoint("${property.street} ${property.buildingNumber}, ${property.city}")?.let {
                    binding.mapFragment.controller.setCenter(it)
                    binding.mapFragment.controller.setZoom(12.0)
                }
            }
            binding.mapFragment.invalidate()
        }
    }

    private fun addOfferMarker(offer: Offer, location: GeoPoint) {
        val marker = Marker(binding.mapFragment).apply {
            position = location
            title = offer.property.street
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            infoWindow = object : InfoWindow(R.layout.info_window_layout, binding.mapFragment) {
                override fun onOpen(item: Any?) {
                    val titleView = mView.findViewById<TextView>(R.id.info_window_title)
                    val descriptionView = mView.findViewById<TextView>(R.id.info_window_description)
                    val button = mView.findViewById<Button>(R.id.info_window_button)

                    titleView.text = offer.property.street
                    descriptionView.text = offer.getOfferDescription()

                    button.setOnClickListener {
                        viewModel.selectedOffer = offer
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, OfferDetailFragment())
                            .addToBackStack(null)
                            .commit()
                        close()
                    }
                }

                override fun onClose() {
                    // Implementation if needed
                }
            }
            setOnMarkerClickListener { clickedMarker, _ ->
                if (!clickedMarker.isInfoWindowShown) clickedMarker.showInfoWindow()
                else clickedMarker.closeInfoWindow()
                true
            }
        }
        binding.mapFragment.overlays.add(marker)
    }

    private fun Offer.getOfferDescription(): String = """
        |Date Issued: $dateIssue
        |Status: $status
        |Price: $price
        |Description: $description
        |Rental Period: $rentalPeriod
        |Interested People: $interestedPeople
        |Property: ${property.getPropertyDescription()}
    """.trimMargin()

    private fun Property.getPropertyDescription(): String = """
        |Voivodeship: $voivodeship
        |City: $city
        |District: $district
        |Street: $street $buildingNumber
        |Area: ${area}m²
        |Max Residents: $maxResidents
        |Construction Year: $constructionYear
        |Number of Rooms: $numberOfRooms
        |Equipment: ${equipment.joinToString(", ")}
        |Images: ${images.joinToString { it.toString() }}
    """.trimMargin()



    private suspend fun addressToGeoPoint(address: String): GeoPoint? = withContext(Dispatchers.IO) {
        try {
            val baseUrl = "https://nominatim.openstreetmap.org/search"
            val format = "json"
            val url = "$baseUrl?q=${address.replace(" ", "+")}&format=$format&limit=1"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Flats4UsApp")
            val response = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            JSONArray(response).optJSONObject(0)?.let {
                GeoPoint(it.getDouble("lat"), it.getDouble("lon"))
            }
        } catch (e: Exception) {
            null
        }
    }
}
