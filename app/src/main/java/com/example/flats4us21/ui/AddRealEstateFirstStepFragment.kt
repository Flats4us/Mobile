package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateFirstStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel


class AddRealEstateFirstStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateFirstStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var selectedItem :String
    private lateinit var DEFAULT_PROPERTY_TYPE : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateFirstStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DEFAULT_PROPERTY_TYPE = requireContext().getString(R.string.real_estate_type)
        val propertyTypeSpinner = binding.propertyTypeSpinner
        val propertyTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            PropertyType.values().map{it.name}
        )
        propertyTypeAdapter.insert(DEFAULT_PROPERTY_TYPE, 0)
        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyTypeSpinner.adapter = propertyTypeAdapter
        setEditText(propertyTypeAdapter)

        propertyTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        realEstateViewModel.fetchVoivodeships()
//        val voivodeshipSuggestionsLiveData = realEstateViewModel.voivodeshipSuggestions
//        val voivodeshipSuggestions = voivodeshipSuggestionsLiveData.value ?: emptyList()
        Log.d("AutoCompleteTextView", "Voivodeship suggestions: $realEstateViewModel.voivodeshipSuggestions")
        val voivodeshipAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            realEstateViewModel.voivodeshipSuggestions
        )
        binding.voivodeship.setAdapter(voivodeshipAdapter)

        binding.nextButton.setOnClickListener {
            collectData()
        }

    }

    private fun setEditText(adapter : ArrayAdapter<String>){
        binding.propertyTypeSpinner.setSelection(adapter.getPosition(realEstateViewModel.propertyType))
        binding.voivodeship.setText(realEstateViewModel.voivodeship)
        binding.city.setText(realEstateViewModel.city)
        binding.district.setText(realEstateViewModel.district)
        binding.street.setText(realEstateViewModel.street)
        binding.buildingNumber.setText(realEstateViewModel.buildingNumber)
    }

    private fun collectData(){
        var test = true
        if(selectedItem == DEFAULT_PROPERTY_TYPE){
            binding.layoutPropertyType.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.propertyType = selectedItem
            binding.layoutPropertyType.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.voivodeship.text.toString().isEmpty() || !realEstateViewModel.checkVoivodeships(binding.voivodeship.text.toString())){
            binding.layoutVoivodeship.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.voivodeship = binding.voivodeship.text.toString()
            binding.layoutVoivodeship.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.city.text.toString().isEmpty()){
            binding.layoutCity.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.city = binding.city.text.toString()
            binding.layoutCity.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        realEstateViewModel.district = binding.district.text.toString()
        if(binding.street.text.toString() == ""){
            binding.layoutStreet.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.street = binding.street.text.toString()
            binding.layoutStreet.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }
        if(binding.buildingNumber.text.toString() == ""){
            binding.layoutBuildingNumber.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            test = false
        }
        else{
            realEstateViewModel.buildingNumber = binding.buildingNumber.text.toString()
            binding.layoutBuildingNumber.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
        }

        if(test){
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateSecondStepFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}