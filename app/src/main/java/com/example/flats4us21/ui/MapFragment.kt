package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.flats4us21.data.RentalPlace
import com.example.flats4us21.databinding.ActivityMapBinding
import com.example.flats4us21.viewmodels.MapViewModel
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

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

        return binding.root
    }

    private fun showAvailablePlacesForRent(rentals: List<RentalPlace>) {
        for (rental in rentals) {
            addRentalMarker(rental.name, GeoPoint(rental.latitude, rental.longitude))
        }

        if (rentals.isNotEmpty()) {
            val firstRental = rentals[0]
            binding.mapFragment.controller.setCenter(GeoPoint(firstRental.latitude, firstRental.longitude))
            binding.mapFragment.controller.setZoom(12.0)
        }
    }

    private fun addRentalMarker(name: String, location: GeoPoint) {
        val marker = Marker(binding.mapFragment)
        marker.position = location
        marker.title = name
        binding.mapFragment.overlays.add(marker)
        binding.mapFragment.invalidate()
    }
}