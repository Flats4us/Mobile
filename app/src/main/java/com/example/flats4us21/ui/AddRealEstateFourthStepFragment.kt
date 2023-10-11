package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateFourthStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateFourthStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateFourthStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateFourthStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        binding.prevButton.setOnClickListener {
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateThirdStepFragment())
        }
        binding.addPropertyButton.setOnClickListener {
            val property = realEstateViewModel.createRealEstateObject()
            realEstateViewModel.addProperty(property)
            Toast.makeText(requireContext(), "Utworzono nieruchomość", Toast.LENGTH_SHORT).show()
            val fragment = SearchFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.resetButton.setOnClickListener {
            reset()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
        }
    }

    private fun bindData() {
        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(realEstateViewModel.images)
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCount = imageSlider.adapter?.itemCount ?: 0
                val currentImage = position + 1
                val imageText = "Image $currentImage of $imageCount"
                binding.imageCount.text = imageText
            }
        })
        binding.voivodeship.text = realEstateViewModel.voivodeship
        binding.city.text = realEstateViewModel.city
        if(realEstateViewModel.getDistricts(realEstateViewModel.city).size != 0){
            binding.district.text = realEstateViewModel.district
            binding.layoutDistrict.isVisible = true
        } else {
            binding.layoutDistrict.isVisible = false
        }
        binding.street.text = realEstateViewModel.street
        binding.buildingNumber.text = realEstateViewModel.buildingNumber
        if(realEstateViewModel.propertyType == PropertyType.FLAT.toString()){
            binding.floor.text = realEstateViewModel.floor
            binding.layoutFloor.isVisible = true
        } else {
            binding.layoutFloor.isVisible = false
        }
        if(realEstateViewModel.propertyType == PropertyType.FLAT.toString()){
            binding.flatNumber.text = realEstateViewModel.flatNumber
            binding.layoutFlatNumber.isVisible = true
        } else {
            binding.layoutFlatNumber.isVisible = false
        }
        binding.area.text = realEstateViewModel.area.toString()

        binding.maxResidents.text = realEstateViewModel.maxResidents.toString()
        if(realEstateViewModel.propertyType == PropertyType.HOUSE.toString()){
            binding.landArea.text = realEstateViewModel.landArea.toString()
            binding.layoutLandArea.isVisible = true
        } else {
            binding.layoutLandArea.isVisible = false
        }
        binding.constructionYear.text = realEstateViewModel.constructionYear.toString()
        if(realEstateViewModel.propertyType != PropertyType.ROOM.toString()){
            binding.numberOfRooms.text = realEstateViewModel.landArea.toString()
            binding.layoutNumberOfRooms.isVisible = true
        } else {
            binding.layoutNumberOfRooms.isVisible = false
        }
        if(realEstateViewModel.propertyType != PropertyType.ROOM.toString()){
            binding.numberOfFloors.text = realEstateViewModel.landArea.toString()
            binding.layoutNumberOfFloors.isVisible = true
        } else {
            binding.layoutNumberOfFloors.isVisible = false
        }
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in realEstateViewModel.equipment.indices){
            stringBuilder.append(realEstateViewModel.equipment[j])

            if(j != realEstateViewModel.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        binding.equipment.text = stringBuilder.toString()
    }

    private fun reset() {
        realEstateViewModel.propertyType = null
        realEstateViewModel.voivodeship = ""
        realEstateViewModel.city = ""
        realEstateViewModel.district = ""
        realEstateViewModel.street = ""
        realEstateViewModel.buildingNumber = ""
        realEstateViewModel.area = 0
        realEstateViewModel.maxResidents = 0
        realEstateViewModel.constructionYear = 0
        realEstateViewModel.numberOfRooms = 0
        realEstateViewModel.numberOfFloors = 0
        realEstateViewModel.equipment = mutableListOf()
        realEstateViewModel.images.clear()
    }

}