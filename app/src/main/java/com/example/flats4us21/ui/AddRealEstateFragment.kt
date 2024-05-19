package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.*
import com.example.flats4us21.data.Property
import com.example.flats4us21.databinding.FragmentAddRealEstateBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateFragment : Fragment() {
    private var _binding : FragmentAddRealEstateBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realEstateViewModel.isCreating = arguments?.getBoolean(IS_CREATING, false)!!
        if(realEstateViewModel.isCreating){
            binding.title.setText(R.string.add_real_estate)
        } else {
            binding.title.setText(R.string.update_real_estate)
            realEstateViewModel.propertyId = arguments?.getInt(PROPERTY_ID)!!
            setData(realEstateViewModel.selectedProperty!!)
        }
        replaceFragment(AddRealEstateFirstStepFragment())
        increaseProgressBar()
    }

    private fun setData(property : Property) {
        realEstateViewModel.voivodeship = property.voivodeship
        realEstateViewModel.city = property.city
        realEstateViewModel.district = property.district
        realEstateViewModel.street = property.street
        realEstateViewModel.postalCode = property.postalCode
        realEstateViewModel.buildingNumber = property.buildingNumber
        realEstateViewModel.area = property.area
        realEstateViewModel.maxResidents = property.maxNumberOfInhabitants
        realEstateViewModel.constructionYear = property.constructionYear
        realEstateViewModel.numberOfRooms = property.numberOfRooms
        realEstateViewModel.equipment = property.equipment.map { e -> e.equipmentId } as MutableList<Int>
        realEstateViewModel.images = property.images as MutableList<Image>
        when(property){
            is Flat -> {
                val flat : Flat = property
                realEstateViewModel.propertyType = PropertyType.FLAT
                realEstateViewModel.floor = flat.floor
                realEstateViewModel.flatNumber = flat.flatNumber.toString()
            }
            is House -> {
                val house : House = property
                realEstateViewModel.propertyType = PropertyType.HOUSE
                realEstateViewModel.landArea = house.landArea
                realEstateViewModel.numberOfFloors = house.numberOfFloors
            }
            is Room -> {
                val room : Room = property
                realEstateViewModel.propertyType = PropertyType.ROOM
                realEstateViewModel.floor = room.floor
                realEstateViewModel.flatNumber = room.flatNumber.toString()
            }
        }
    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.addRealEstateStep, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun increaseProgressBar(){
        binding.progressBar.incrementProgressBy(25)
    }

    fun decreaseProgressBar(){
        binding.progressBar.incrementProgressBy(-25)
    }

    fun decreaseProgressBar(diff: Int){
        binding.progressBar.incrementProgressBy(diff)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}