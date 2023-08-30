package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        binding.prevButton.setOnClickListener {
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateThirdStepFragment())
        }
        binding.addPropertyButton.setOnClickListener {
            val property = realEstateViewModel.createRealEstateObject()
        }
        binding.resetButton.setOnClickListener {
            reset()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
        }
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
        realEstateViewModel.equipment = ""
        realEstateViewModel.images.clear()
    }

}