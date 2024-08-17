package com.example.flats4us21.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Flat
import com.example.flats4us21.data.House
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.Room
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentOfferDetailBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import com.example.flats4us21.viewmodels.OfferViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import java.time.Period
import java.util.Locale

private const val TAG = "OfferDetailFragment"
class OfferDetailFragment : Fragment() {
    private var _binding : FragmentOfferDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel
    private lateinit var detailOfferViewModel: DetailOfferViewModel
    private lateinit var addButton: ImageButton
    private var isObserved = false
    private lateinit var offer: Offer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offerId = arguments?.getInt(OFFER_ID, -1)

        addButton = binding.addButton
        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        detailOfferViewModel = ViewModelProvider(this)[DetailOfferViewModel::class.java]

        detailOfferViewModel.getOfferDetails(offerId!!)

        detailOfferViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        detailOfferViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                detailOfferViewModel.clearErrorMessage()
            }
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            resultMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(resultMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    resultMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
            }
            addButton.setOnClickListener {
                if (isObserved ) {
                    addButton.setImageResource(R.drawable.unobserve)
                    viewModel.unwatchOffer(offerId)
                    isObserved = false
                } else if(!isObserved){
                    addButton.setImageResource(R.drawable.observe)
                    viewModel.watchOffer(offerId)
                    isObserved = true
                }
                isObserved = !isObserved
            }
        }

        detailOfferViewModel.offer.observe(viewLifecycleOwner) { offer ->
            this.offer = offer
            bindOfferData(offer)
        }

        binding.fab.setOnClickListener {
            showDialog(offerId)
        }

        binding.reviewsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PROPERTY_ID, offer.property.propertyId)
            val fragment = PropertyOpinionsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        isObserved = offer.isInterest

        if(offer.isInterest){
            binding.addButton.setImageResource(R.drawable.observe)
        } else{
            binding.addButton.setImageResource(R.drawable.unobserve)
        }

        binding.fab.isVisible = DataStoreManager.userRole.value?.let { it == "Student" } ?: false
        binding.addButton.isVisible = DataStoreManager.userRole.value?.let { it == "Student" } ?: false

        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(offer.property.images)
        val imageCount = imageSlider.adapter?.itemCount ?: 0
        var imageText = "${if(offer.property.images.isNotEmpty()) 1 else 0}/$imageCount"
        binding.imageCount.text = imageText
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentImage = position + 1
                imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })
        val url = "$URL/${offer.owner.profilePicture?.path}"
        Log.i(TAG, url)
        binding.ownerPhoto.load(url) {
            error(R.drawable.baseline_person_24)
        }
        if(offer.surveyOwnerOffer.smokingAllowed){
            binding.smoking.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
        } else {
            binding.smoking.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
        }
        binding.smoking.setOnClickListener {
            if(offer.surveyOwnerOffer.smokingAllowed) {
                Toast.makeText(requireContext(), getString(R.string.smoking_allowed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.smoking_not_allowed), Toast.LENGTH_SHORT).show()
            }
        }
        if(offer.surveyOwnerOffer.animalsAllowed) {
            binding.pets.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
        } else {
            binding.pets.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
        }
        binding.pets.setOnClickListener {
            if(offer.surveyOwnerOffer.animalsAllowed) {
                Toast.makeText(requireContext(), getString(R.string.animals_allowed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.animals_not_allowed), Toast.LENGTH_SHORT).show()
            }
        }
        if(offer.surveyOwnerOffer.partiesAllowed){
            binding.parties.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
        } else {
            binding.parties.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
        }
        binding.parties.setOnClickListener {
            if(offer.surveyOwnerOffer.partiesAllowed){
                Toast.makeText(requireContext(), getString(R.string.parties_allowed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.parties_not_allowed), Toast.LENGTH_SHORT).show()
            }
        }

        binding.owner.text = getString(R.string.name_and_surname, offer.owner.name, offer.owner.surname)
        binding.ownerLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(USER_ID, offer.owner.id)
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.startDate.text = offer.startDate.toString()
        binding.endDate.text = offer.endDate.toString()
        binding.price.text = offer.price
        binding.deposit.text = offer.deposit
        binding.city.text = offer.property.city
        binding.district.text = offer.property.district
        binding.street.text = " ${offer.property.street} ${offer.property.buildingNumber}"
        binding.area.text = offer.property.area.toString()
        binding.numberOfRooms.text = offer.property.numberOfRooms.toString()
        val period = Period.between(offer.startDate, offer.endDate)
        val monthsBetween = period.years * 12 + period.months
        binding.period.text = monthsBetween.toString()
        binding.maxResidents.text = offer.property.maxNumberOfInhabitants.toString()
        binding.constructionYear.text = offer.property.constructionYear.toString()
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in offer.property.equipment.indices){
            stringBuilder.append(QuestionTranslator.translateEquipmentName(offer.property.equipment[j].equipmentName.lowercase(Locale.getDefault()), requireContext()))

            if(j != offer.property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        if(offer.property.equipment.isEmpty()){
            stringBuilder.append(getString(R.string.missing))
        }

        if(offer.property.avgRating == 0){
            binding.emptyView.visibility = View.VISIBLE
            binding.opinionLayout.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.opinionLayout.visibility = View.VISIBLE
        }

        binding.equipment.text = stringBuilder.toString()
        binding.rules.text = offer.userRegulation
        setMap(offer)
        binding.description.text = offer.description
        binding.ratingBar.rating = (offer.property.avgRating.toFloat() / 2)
        binding.sumService.text = offer.property.avgServiceRating.toString()
        binding.sumLocation.text = offer.property.avgLocationRating.toString()
        binding.sumEquipment.text = offer.property.avgEquipmentRating.toString()
        binding.sumQualityForMoney.text = offer.property.avgQualityForMoneyRating.toString()

        when(offer.property){
            is House -> {
                binding.title.text = PropertyType.HOUSE.toLocalizedString(requireContext())
                val house: House = offer.property
                binding.landArea.text = house.landArea.toString()
                binding.landAreaLayout.visibility = View.VISIBLE
            }
            is Flat -> {
                binding.title.text = PropertyType.FLAT.toLocalizedString(requireContext())
            }

            is Room -> {
                binding.title.text = PropertyType.ROOM.toLocalizedString(requireContext())
            }
        }
    }

    private fun setMap(offer: Offer) {
        binding.mapFragment.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
        }

        lifecycleScope.launch {
            val location = getGeoPoint(offer)
            location?.let {
                addOfferMarker(offer, it)
            }
        }
    }

    private fun addOfferMarker(offer: Offer, location: GeoPoint) {
        val marker = Marker(binding.mapFragment).apply {
            position = location
            title = offer.offerId.toString()
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            setOnMarkerClickListener { _, _ ->
                showDialog(offer.offerId)
                true
            }
        }
        val mapController = binding.mapFragment.controller
        mapController.setZoom(15.5)
        mapController.setCenter(location)
        binding.mapFragment.overlays.add(marker)
    }

    private suspend fun getGeoPoint(offer: Offer): GeoPoint? = withContext(Dispatchers.IO) {
        try {
            GeoPoint(offer.property.geoLat, offer.property.geoLon)
        } catch (e: Exception) {
            null
        }
    }

    private fun showDialog(offerId: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_offer_layout)

        val rentButton = dialog.findViewById<View>(R.id.rentButton)
        val meetButton = dialog.findViewById<View>(R.id.meetButton)

        rentButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, offerId)
            val realEstateRentalDialogFragment = RealEstateRentalDialogFragment(detailOfferViewModel)
            realEstateRentalDialogFragment.arguments = bundle
            realEstateRentalDialogFragment.show(parentFragmentManager , "RealEstateRentalDialogFragment")

            detailOfferViewModel.newRent.observe(viewLifecycleOwner) { rent ->
                if(rent != null){
                    Log.d(TAG, rent.toString())
                }
            }
            dialog.dismiss()
        }
        meetButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, offerId)
            val fragment = AddMeetingFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}