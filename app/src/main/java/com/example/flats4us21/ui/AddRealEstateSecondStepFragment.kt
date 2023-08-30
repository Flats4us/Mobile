package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentAddRealEstateSecondStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateSecondStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateSecondStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private var test : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateSecondStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditText()
        binding.prevButton.setOnClickListener {
            collectData()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
        }
        binding.nextButton.setOnClickListener {
            collectData()
            if(test){
                (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateThirdStepFragment())
            }
        }
    }

    private fun setEditText(){
        if(realEstateViewModel.area != 0){
            binding.area.setText(realEstateViewModel.area.toString())
        }
        if(realEstateViewModel.maxResidents != 0){
            binding.maxResidents.setText(realEstateViewModel.maxResidents.toString())
        }
        if(realEstateViewModel.constructionYear != 0){
            binding.constructionYear.setText(realEstateViewModel.constructionYear.toString())
        }
        if(realEstateViewModel.numberOfRooms != 0){
            binding.numberOfRooms.setText(realEstateViewModel.numberOfRooms.toString())
        }
        if(realEstateViewModel.numberOfFloors != 0){
            binding.numberOfFloors.setText(realEstateViewModel.numberOfFloors.toString())
        }
        binding.equipment.setText(realEstateViewModel.equipment)

    }

    private fun collectData() {
        test = true
        if(binding.area.text.toString().isEmpty()){
            binding.layoutArea.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.area = binding.area.text.toString().toIntOrNull() ?: 0
            binding.layoutArea.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.maxResidents.text.toString().isEmpty()){
            binding.layoutMaxResidents.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.maxResidents = binding.maxResidents.text.toString().toIntOrNull() ?: 0
            binding.layoutMaxResidents.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.constructionYear.text.toString().isEmpty()){
            binding.layoutConstructionYear.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.constructionYear = binding.constructionYear.text.toString().toIntOrNull() ?: 0
            binding.layoutConstructionYear.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.numberOfRooms.text.toString().isEmpty()){
            binding.layoutNumberOfRooms.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.numberOfRooms = binding.numberOfRooms.text.toString().toIntOrNull() ?: 0
            binding.layoutNumberOfRooms.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.numberOfFloors.text.toString().isEmpty()){
            binding.layoutNumberOfFloors.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.numberOfFloors = binding.numberOfFloors.text.toString().toIntOrNull() ?: 0
            binding.layoutNumberOfFloors.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        realEstateViewModel.equipment = binding.equipment.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}