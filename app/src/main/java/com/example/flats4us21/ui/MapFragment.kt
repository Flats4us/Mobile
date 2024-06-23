package com.example.flats4us21.ui

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.MapOffer
import com.example.flats4us21.databinding.FragmentMapBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "MapFragment"

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[OfferViewModel::class.java]

        Configuration.getInstance().load(
            context, PreferenceManager.getDefaultSharedPreferences(context)
        )

        setupMap()
        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupMap() {
        binding.mapFragment.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
        }
    }

    private fun setupObservers() {
        viewModel.getOffersForMap()
        viewModel.mapOffers.observe(viewLifecycleOwner) { offers ->
            showAvailableOffers(offers)
        }
    }

    private fun setupListeners() {
        binding.addressEditText.onEnterKeyPressed {
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
    }

    private fun EditText.onEnterKeyPressed(callback: () -> Unit) {
        setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                callback()
                true
            } else {
                false
            }
        }
    }

    private fun showAvailableOffers(offers: List<MapOffer>) {
        lifecycleScope.launch {
            binding.mapFragment.overlays.clear()
            offers.forEach { offer ->
                val location = getGeoPoint(offer)
                location?.let {
                    addOfferMarker(offer, it)
                } ?: Toast.makeText(context, "Address not found for", Toast.LENGTH_SHORT).show()
            }
            offers.firstOrNull()?.let { offer ->
                getGeoPoint(offer)?.let {
                    binding.mapFragment.controller.setCenter(it)
                    binding.mapFragment.controller.setZoom(12.0)
                }
            }
            binding.mapFragment.invalidate()
        }
    }

    private fun addOfferMarker(offer: MapOffer, location: GeoPoint) {
        val marker = Marker(binding.mapFragment).apply {
            position = location
            title = offer.offerId.toString()
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            setOnMarkerClickListener { _, _ ->
                showDialog(offer.offerId)
                true
            }
        }
        binding.mapFragment.overlays.add(marker)
    }

    private suspend fun getGeoPoint(offer: MapOffer): GeoPoint? = withContext(Dispatchers.IO) {
        try {
            GeoPoint(offer.property.geoLat, offer.property.geoLon)
        } catch (e: Exception) {
            null
        }
    }

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

    private fun showDialog(offerId: Int) {
        viewModel.getOffer(offerId)

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottom_sheet_offer_preview_layout)
        }

        val imageSlider = dialog.findViewById<ViewPager2>(R.id.image)
        val imageCountTV: TextView = dialog.findViewById(R.id.imageCount)
        val ownerPhoto: ImageView = dialog.findViewById(R.id.ownerPhoto)
        val owner: TextView = dialog.findViewById(R.id.owner)
        val startDate: TextView = dialog.findViewById(R.id.startDate)
        val endDate: TextView = dialog.findViewById(R.id.endDate)
        val price: TextView = dialog.findViewById(R.id.price)
        val deposit: TextView = dialog.findViewById(R.id.deposit)
        val city: TextView = dialog.findViewById(R.id.city)
        val district: TextView = dialog.findViewById(R.id.district)
        val street: TextView = dialog.findViewById(R.id.street)
        val area: TextView = dialog.findViewById(R.id.area)
        val numberOfRooms: TextView = dialog.findViewById(R.id.numberOfRooms)
        val offerDetailButton: Button = dialog.findViewById(R.id.see_details)

        viewModel.offer.observe(viewLifecycleOwner) { offer ->
            imageSlider.adapter = ImageSliderAdapter(offer!!.property.images)
            imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val imageCount = imageSlider.adapter?.itemCount ?: 0
                    val currentImage = position + 1
                    val imageText = "$currentImage/$imageCount"
                    imageCountTV.text = imageText
                }
            })
            ownerPhoto.load("$URL/${offer.owner.profilePicture?.path}") {
                error(R.drawable.baseline_person_24)
            }
            owner.text = getString(R.string.name_and_surname, offer.owner.name, offer.owner.surname)
            startDate.text = offer.dateIssue
            endDate.text = offer.dateIssue
            price.text = offer.price
            deposit.text = offer.deposit
            city.text = offer.property.city
            district.text = offer.property.district
            street.text = " ${offer.property.street} ${offer.property.buildingNumber}"
            area.text = offer.property.area.toString()
            numberOfRooms.text = offer.property.numberOfRooms.toString()
            offerDetailButton.setOnClickListener {
                val bundle = Bundle().apply { putInt(OFFER_ID, offer.offerId) }
                val fragment = OfferDetailFragment().apply { arguments = bundle }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
                dialog.dismiss()
            }
        }

        dialog.apply {
            show()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }
}
