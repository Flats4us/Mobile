package com.example.flats4us21.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateFirstStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateFirstStepFragment : Fragment() {
    private var _binding: FragmentAddRealEstateFirstStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private var selectedProperty: String = ""
    private var selectedVoivodeship: String = ""
    private var selectedDistrict: String = ""
    private lateinit var DEFAULT_PROPERTY_TYPE: String
    private lateinit var propertyTypeAdapter : ArrayAdapter<String>
    private lateinit var voivodeshipAdapter : ArrayAdapter<String>
    private lateinit var districtAdapter : ArrayAdapter<String>

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
        DEFAULT_PROPERTY_TYPE = getString(R.string.choose_an_option)

        setupPropertyTypeSpinner()
        setupVoivodeshipSpinner()
        setupDistrictSpinner()

        setValues()

        binding.nextButton.setOnClickListener { collectData() }
    }

    private fun setupPropertyTypeSpinner() {
        val propertyTypeSpinner = binding.propertyTypeSpinner
        propertyTypeAdapter = createSpinnerAdapter(PropertyType.values().map { it.name })
        propertyTypeSpinner.adapter = propertyTypeAdapter

        val onItemSelectedListener = createOnItemSelectedListener {
            selectedProperty = it
            if(selectedProperty == PropertyType.FLAT.toString()){
                binding.layoutFloorWithHeader.isVisible = true
                binding.layoutFlatNumberWithHeader.isVisible = true
            } else {
                binding.layoutFloorWithHeader.isVisible = false
                binding.layoutFlatNumberWithHeader.isVisible = false
            }
        }
        propertyTypeSpinner.onItemSelectedListener = onItemSelectedListener

    }

    private fun setupVoivodeshipSpinner() {
        val voivodeship = binding.voivodeship
        voivodeshipAdapter = createSpinnerAdapter(realEstateViewModel.voivodeshipSuggestions)
        voivodeship.adapter = voivodeshipAdapter

        val onItemSelectedListener = createOnItemSelectedListener { selectedVoivodeship = it }
        voivodeship.onItemSelectedListener = onItemSelectedListener

        realEstateViewModel.fetchVoivodeships()
    }

    private fun setupDistrictSpinner() {
        val district = binding.district
        val cityEditText = binding.city
        val districtData = mutableListOf<String>()
        districtAdapter = createSpinnerAdapter(districtData)

        district.adapter = districtAdapter

        cityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val city = s.toString()
                if (city.isNotEmpty()) {
                    val districts = realEstateViewModel.getDistricts(city)
                    if (districts.isNotEmpty()){
                        districtData.clear()
                        districtData.addAll(districts)

                        updateDistrictSpinner(districtData)
                        district.isEnabled = true
                    } else {
                        district.isEnabled = false
                        realEstateViewModel.district = ""
                    }
                } else {
                    districtData.clear()
                    updateDistrictSpinner(districtData)
                    district.isEnabled = false
                }
            }
        })

        val onItemSelectedListener = createOnItemSelectedListener { selectedDistrict = it }
        district.onItemSelectedListener = onItemSelectedListener
    }


    private fun updateDistrictSpinner(districts: List<String>) {
        val districtAdapter = createSpinnerAdapter(districts)
        binding.district.adapter = districtAdapter
        binding.district.isEnabled = true

    }
    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        adapter.insert(DEFAULT_PROPERTY_TYPE, 0)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun createOnItemSelectedListener(action: (String) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action(parent?.getItemAtPosition(position) as String)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setValues() {
        with(binding) {
            propertyTypeSpinner.setSelection(propertyTypeAdapter.getPosition(realEstateViewModel.propertyType))
            voivodeship.setSelection(voivodeshipAdapter.getPosition(realEstateViewModel.voivodeship))
            city.setText(realEstateViewModel.city)
            val districts = realEstateViewModel.getDistricts(city.text.toString())
            if(districts.isEmpty()) {
                district.isEnabled = false
            } else {
                updateDistrictSpinner(districts)
                district.setSelection(districtAdapter.getPosition(realEstateViewModel.district))
            }
            street.setText(realEstateViewModel.street)
            buildingNumber.setText(realEstateViewModel.buildingNumber)
            if(realEstateViewModel.propertyType.equals(PropertyType.FLAT.toString())){
                floor.setText(realEstateViewModel.floor)
                layoutFloorWithHeader.isVisible = true
                flatNumber.setText(realEstateViewModel.flatNumber)
                layoutFlatNumberWithHeader.isVisible = true
            }
        }
    }

    private fun collectData() {
        val isPropertyTypeValid = setSelectedItemAndValidate(binding.propertyTypeSpinner, binding.layoutPropertyType, selectedProperty) { value ->
            realEstateViewModel.propertyType = value
        }
        val isVoivodeshipValid = setSelectedItemAndValidate(binding.voivodeship, binding.layoutVoivodeship, selectedVoivodeship) { value ->
            realEstateViewModel.voivodeship = value
        }
        val isCityValid = setAndValidateText(binding.city, binding.layoutCity) { value ->
            realEstateViewModel.city = value
        }
        val isDistrictValid = setSelectedItemAndValidate(binding.district, binding.layoutDistrict, selectedDistrict) { value ->
            realEstateViewModel.district = value
        }
        val isStreetValid = setAndValidateText(binding.street, binding.layoutStreet) { value ->
            realEstateViewModel.street = value
        }
        val isBuildingNumberValid = setAndValidateText(binding.buildingNumber, binding.layoutBuildingNumber) { value ->
            realEstateViewModel.buildingNumber = value
        }
        val isFloorValid  = setAndValidateOptionalText(binding.floor, binding.layoutFloor, binding.layoutFloorWithHeader) { value ->
            realEstateViewModel.floor = value
        }
        val isFlatNumberValid  = setAndValidateOptionalText(binding.flatNumber, binding.layoutFlatNumber, binding.layoutFlatNumberWithHeader) { value ->
            realEstateViewModel.flatNumber = value
        }

        if (isPropertyTypeValid && isVoivodeshipValid && isCityValid && isDistrictValid && isStreetValid && isBuildingNumberValid && isFloorValid && isFlatNumberValid) {
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateSecondStepFragment())
            (requireParentFragment() as AddRealEstateFragment).increaseProgressBar()
        }
    }

    private fun setSelectedItemAndValidate(spinner : Spinner, spinnerLayout : ViewGroup, selectedItem: String, targetProperty: (String) -> Unit): Boolean {
        val isValid = selectedItem != DEFAULT_PROPERTY_TYPE
        if (isValid) {
            targetProperty(selectedItem)
        }
        spinnerLayout.setBackgroundResource(if (isValid || !spinner.isEnabled) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !spinner.isEnabled
    }

    private fun setAndValidateText(editText: EditText, editTextLayout : ViewGroup, targetProperty: (String) -> Unit): Boolean {
        val text = editText.text.toString()
        val isValid = text.isNotEmpty()
        if (isValid) {
            targetProperty(text)
        }
        editTextLayout.setBackgroundResource(if (isValid || !editTextLayout.isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !editTextLayout.isVisible
    }

    private fun setAndValidateOptionalText(editText: EditText, editTextLayout : ViewGroup, editTextLayoutWithHeader : ViewGroup, targetProperty: (String) -> Unit): Boolean {
        val text = editText.text.toString()
        val isValid = text.isNotEmpty()
        if (isValid) {
            targetProperty(text)
        }
        editTextLayout.setBackgroundResource(if (isValid || !editTextLayoutWithHeader.isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !editTextLayoutWithHeader.isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}