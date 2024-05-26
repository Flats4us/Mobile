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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.example.flats4us21.data.Room
import com.example.flats4us21.databinding.FragmentOfferDetailBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import com.example.flats4us21.viewmodels.OfferViewModel
import java.time.Period

private const val TAG = "OfferDetailFragment"
class OfferDetailFragment : Fragment() {
    private var _binding : FragmentOfferDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel
    private lateinit var detailOfferViewModel: DetailOfferViewModel
    private lateinit var addButton: ImageButton
    private var isObserved = false

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
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            if(resultMessage != null) {
                Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
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
            bindOfferData(offer)
        }

        binding.fab.setOnClickListener {
            showDialog(offerId)
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
            stringBuilder.append(offer.property.equipment[j].equipmentName)

            if(j != offer.property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        if(offer.property.equipment.isEmpty()){
            stringBuilder.append("BRAK")
        }
        binding.equipment.text = stringBuilder.toString()
        binding.description.text = offer.description
        binding.interestedPeople.text = offer.interestedPeople.toString()

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

    private fun showDialog(offerId: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_offer_layout)

        val layoutRent = dialog.findViewById<View>(R.id.layoutRent)
        val layoutChat = dialog.findViewById<View>(R.id.layoutChat)
        val layoutMeet = dialog.findViewById<View>(R.id.layoutMeet)

        layoutRent.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, offerId)
            val realEstateRentalDialogFragment = RealEstateRentalDialogFragment(detailOfferViewModel)
            realEstateRentalDialogFragment.arguments = bundle
            realEstateRentalDialogFragment.show(parentFragmentManager , "RealEstateRentalDialogFragment")

            detailOfferViewModel.newRent.observe(viewLifecycleOwner) { rent ->
                if(rent != null){
                    Log.d("OfferDetailFragment", rent.toString())
                }
            }
        }
        layoutChat.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked Napisz", Toast.LENGTH_SHORT).show()
        }
        layoutMeet.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked Umów spotkanie", Toast.LENGTH_SHORT).show()
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