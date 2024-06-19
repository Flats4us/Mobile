package com.example.flats4us21.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Flat
import com.example.flats4us21.data.House
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Room
import com.example.flats4us21.databinding.FragmentOwnerOfferDetailBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import com.example.flats4us21.viewmodels.OfferViewModel
import java.time.Period

const val RENT_PROPOSITION_ID = "RENT_PROPOSITION_ID"
private const val TAG = "OwnerOfferDetailFragment"
class OwnerOfferDetailFragment : Fragment() {
    private var _binding : FragmentOwnerOfferDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel
    private lateinit var detailOfferViewModel: DetailOfferViewModel
    private lateinit var currentOffer : Offer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerOfferDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offerId = arguments?.getInt(OFFER_ID, -1)

        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        detailOfferViewModel = ViewModelProvider(this)[DetailOfferViewModel::class.java]

        detailOfferViewModel.getOfferDetails(offerId!!)

        detailOfferViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        detailOfferViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        detailOfferViewModel.offer.observe(viewLifecycleOwner) { offer ->
            currentOffer = offer
            bindOfferData(currentOffer)
        }

        binding.reviewsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PROPERTY_ID, currentOffer.property.propertyId)
            val fragment = PropertyOpinionsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.fab.setOnClickListener {
            showDialog()
        }
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(offer.property.images)
        val imageCount = imageSlider.adapter?.itemCount ?: 0
        var imageText = "1/$imageCount"
        binding.imageCount.text = imageText
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentImage = position + 1
                imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })
        binding.startDate.text = offer.dateIssue
        binding.endDate.text = offer.dateIssue
        binding.price.text = offer.price
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
            stringBuilder.append(offer.property.equipment[j].equipmentName)

            if(j != offer.property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        if(offer.property.equipment.isEmpty()){
            stringBuilder.append("BRAK")
        }
        if(offer.property.avgRating == 0){
            binding.emptyView.visibility = View.VISIBLE
            binding.opinionLayout.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.opinionLayout.visibility = View.VISIBLE
        }

        binding.equipment.text = stringBuilder.toString()
        binding.description.text = offer.description
        binding.ratingBar.rating = offer.property.avgRating.toFloat()
        binding.sumService.text = offer.property.avgServiceRating.toString()
        binding.sumLocation.text = offer.property.avgLocationRating.toString()
        binding.sumEquipment.text = offer.property.avgEquipmentRating.toString()
        binding.sumQualityForMoney.text = offer.property.avgQualityForMoneyRating.toString()
        binding.interestedPeople.text = offer.interestedPeople.toString()

        Log.d(TAG, "rentPropositionToShow: ${offer.rentPropositionToShow}")
        Log.d(TAG, "offer.rentPropositionToShow != null: ${offer.rentPropositionToShow != null}")

        when(offer.property){
            is House -> {
                val house: House = offer.property
                binding.landArea.text = house.landArea.toString()
                binding.landAreaLayout.visibility = View.VISIBLE
            }
            is Flat -> {}
            is Room -> {}
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_owner_offer_layout)

        val layoutPromote = dialog.findViewById<View>(R.id.layoutPromote)
        val layoutRelatedProperty = dialog.findViewById<View>(R.id.layoutRelatedProperty)
        val layoutEndOffer = dialog.findViewById<View>(R.id.layoutEndOffer)
        val layoutRentProposition = dialog.findViewById<View>(R.id.layoutRentProposition)

        layoutPromote.setOnClickListener {
           viewModel.promoteOffer(currentOffer.offerId)
            dialog.dismiss()
        }
        layoutRelatedProperty.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PROPERTY_ID, currentOffer.property.propertyId)
            val fragment = OwnerPropertyDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
            dialog.dismiss()
        }
        layoutEndOffer.setOnClickListener {
            viewModel.cancelOffer(currentOffer.offerId)
            dialog.dismiss()
        }
        layoutRentProposition.setOnClickListener {
            val fragment = RentPropositionDialogFragment()
            val bundle = Bundle()
            bundle.putInt(RENT_PROPOSITION_ID, currentOffer.rentPropositionToShow!!)
            bundle.putInt(OFFER_ID, currentOffer.offerId)
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        if(currentOffer.isPromoted) {
            layoutPromote.visibility = View.GONE
        } else {
            layoutPromote.visibility = View.VISIBLE
        }
        if(currentOffer.rentPropositionToShow != null){
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.INVISIBLE
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