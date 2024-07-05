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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Flat
import com.example.flats4us21.data.House
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.Room
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentOwnerPropertyDetailBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import java.util.Locale

const val IS_CREATING = "IS_CREATING"
const val PROPERTY_ID = "PROPERTY_ID"
private const val TAG = "OwnerPropertyDetailFragment"
class OwnerPropertyDetailFragment : Fragment() {
    private var _binding : FragmentOwnerPropertyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var property: Property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerPropertyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]

        val propertyId = arguments?.getInt(PROPERTY_ID)
        if(propertyId != null) {
            realEstateViewModel.getProperty(propertyId)
        }
        realEstateViewModel.property.observe(viewLifecycleOwner) {
            if(it != null) {
                property = it
                bindData(it)
            }
        }

        binding.reviewsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, property.propertyId)
            val fragment = PropertyOpinionsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.fab.setOnClickListener {
            showDialog()
        }
    }

    private fun bindData(property: Property) {
        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(property.images)
        val imageCount = imageSlider.adapter?.itemCount ?: 0
        var imageText = "${if(property.images.isNotEmpty()) 1 else 0}/$imageCount"
        binding.imageCount.text = imageText
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentImage = position + 1
                imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })
        binding.voivodeship.text = property.voivodeship
        binding.city.text = property.city
        Log.d(TAG, "onViewCreated: ${property.district}")
        if(property.district != null) {
            binding.district.text = property.district
            binding.layoutDistrict.visibility = View.VISIBLE
        } else {
            binding.layoutDistrict.visibility = View.GONE
        }
        binding.street.text = property.street
        binding.buildingNumber.text = property.buildingNumber
        binding.area.text = property.area.toString()
        binding.maxResidents.text = property.maxNumberOfInhabitants.toString()
        binding.constructionYear.text = property.constructionYear.toString()
        binding.numberOfRooms.text = property.numberOfRooms.toString()
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in property.equipment.indices){
            stringBuilder.append(QuestionTranslator.translateEquipmentName(property.equipment[j].equipmentName.lowercase(Locale.getDefault()), requireContext()))
            if(j != property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        binding.equipment.text = stringBuilder.toString()

        binding.layoutLandArea.isVisible = false
        binding.layoutFloor.isVisible = false
        binding.layoutFlatNumber.isVisible = false
        binding.layoutNumberOfFloors.isVisible = false
        when(property){
            is House -> {
                val house: House = property
                binding.landArea.text = house.landArea.toString()
                binding.layoutLandArea.isVisible = true
                binding.numberOfFloors.text = house.numberOfFloors.toString()
                binding.layoutNumberOfFloors.isVisible = true
            }
            is Flat -> {
                binding.floor.text = property.floor.toString()
                binding.layoutFloor.isVisible = true
                binding.flatNumber.text = property.flatNumber.toString()
                binding.layoutFlatNumber.isVisible = true
            }
            is Room -> {
                binding.layoutNumberOfRooms.isVisible = false
            }
        }

        if(property.avgRating == 0){
            binding.emptyView.visibility = View.VISIBLE
            binding.opinionLayout.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.opinionLayout.visibility = View.VISIBLE
        }

        binding.ratingBar.rating = (property.avgRating.toFloat() / 2)
        binding.sumService.text = property.avgServiceRating.toString()
        binding.sumLocation.text = property.avgLocationRating.toString()
        binding.sumEquipment.text = property.avgEquipmentRating.toString()
        binding.sumQualityForMoney.text = property.avgQualityForMoneyRating.toString()
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_property_layout)

        val editPropertyButton = dialog.findViewById<View>(R.id.editPropertyButton)
        val deletePropertyButton = dialog.findViewById<View>(R.id.deletePropertyButton)
        editPropertyButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(IS_CREATING, false)
            bundle.putInt(PROPERTY_ID, property.propertyId)
            val fragment = AddRealEstateFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        deletePropertyButton.setOnClickListener {
            Log.d(TAG, "offers: ${property.offers} isNullOrEmpty ${!property.offers.isNullOrEmpty()}")
            if(property.offers.isNullOrEmpty()) {
                realEstateViewModel.deleteProperty(property.propertyId) { result ->
                    if (result) {
                        Toast.makeText(requireContext(), getString(R.string.deleted_property), Toast.LENGTH_LONG).show()
                        (activity as? DrawerActivity)!!.goBack()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.property_has_associated_offers), Toast.LENGTH_LONG).show()
            }
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