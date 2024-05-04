package com.example.flats4us21.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentFilterBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import com.example.flats4us21.viewmodels.RealEstateViewModel

private const val TAG = "FilterFragment"
class FilterFragment : Fragment() {
    private var _binding : FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var offerViewModel: OfferViewModel
    private var selectedProperty: String = ""
    private var selectedVoivodeship: String = ""
    private var selectedDistrict: String = ""
    private var selectedSorcting: String = ""
    private lateinit var pickedEquipment: MutableList<Int>
    private lateinit var DEFAULT_PROPERTY_TYPE: String
    private lateinit var sortingAdapter : ArrayAdapter<String>
    private lateinit var propertyTypeAdapter : ArrayAdapter<String>
    private lateinit var voivodeshipAdapter : ArrayAdapter<String>
    private lateinit var districtAdapter : ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        offerViewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DEFAULT_PROPERTY_TYPE = getString(R.string.choose_an_option)
        pickedEquipment = mutableListOf()

        setupSortingSpinner()
        setupPropertyTypeSpinner()
        setupVoivodeshipSpinner()
        setupDistrictSpinner()
        setupEquipment()
        setValues()


        val clearButton = binding.clearButton
        clearButton.setOnClickListener {
            offerViewModel.clearNullableVariables()
            setValues()
        }
        val filterButton = binding.filterButton
        filterButton.setOnClickListener {
            collectData()
            val fragment = SearchFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

    }

    private fun setupSortingSpinner() {
    }

    private fun setupPropertyTypeSpinner() {
        val propertyTypeSpinner = binding.propertyTypeSpinner
        propertyTypeAdapter = createSpinnerAdapter(PropertyType.values().map { it.name })
        propertyTypeSpinner.adapter = propertyTypeAdapter

        val onItemSelectedListener = createOnItemSelectedListener {
            selectedProperty = it
        }
        propertyTypeSpinner.onItemSelectedListener = onItemSelectedListener
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

    private fun setupVoivodeshipSpinner() {
        val voivodeship = binding.voivodeship
        voivodeshipAdapter = createSpinnerAdapter(realEstateViewModel.voivodeshipSuggestions)
        voivodeshipAdapter.insert(DEFAULT_PROPERTY_TYPE, 0)
        voivodeship.adapter = voivodeshipAdapter

        val onItemSelectedListener = createOnItemSelectedListener { selectedVoivodeship = it }
        voivodeship.onItemSelectedListener = onItemSelectedListener

        realEstateViewModel.fetchVoivodeships()
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        if(data.isNotEmpty() && data[0] !=DEFAULT_PROPERTY_TYPE)
            adapter.insert(DEFAULT_PROPERTY_TYPE, 0)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
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
                        binding.districtHeader.setText(R.string.required_district)
                    } else {
                        district.isEnabled = false
                        binding.districtHeader.setText(R.string.district)
                        realEstateViewModel.district = ""
                    }
                } else {
                    districtData.clear()
                    updateDistrictSpinner(districtData)
                    district.isEnabled = false
                    binding.districtHeader.setText(R.string.district)
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

    private fun setupEquipment() {
        val equipment = binding.equipment
        val equipmentList: MutableList<Int> = mutableListOf()
        realEstateViewModel.getEquipmentList()
        realEstateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        realEstateViewModel.equipments.observe(viewLifecycleOwner) { equipments ->
            val equipmentArray = equipments.map { it.equipmentName }.toTypedArray()
            val selectedEquipment = BooleanArray(equipmentArray.size) { index ->
                realEstateViewModel.equipment.contains(index+1)
            }

            equipment.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Wybierz wyposażenie")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(equipmentArray, selectedEquipment) { _, position, isChecked ->
                    selectedEquipment[position] = isChecked

                    if (isChecked) {
                        equipmentList.add(position)
                        equipmentList.sort()
                    } else {
                        equipmentList.remove(position)
                    }
                }
                builder.setPositiveButton("Akcepuj") { _, _ ->
                    for (j in selectedEquipment.indices) {
                        if (selectedEquipment[j] && !pickedEquipment.contains(j + 1)) {
                            pickedEquipment.add(j + 1)
                        } else if (!selectedEquipment[j] && pickedEquipment.contains(j + 1)) {
                            pickedEquipment.remove(j + 1)
                        }
                    }
                }
                builder.setNegativeButton("Anuluj") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setNeutralButton("Wyczyść") { _, _ ->
                    for (j in selectedEquipment.indices) {
                        selectedEquipment[j] = false
                        equipmentList.clear()
                        equipment.text = ""
                        pickedEquipment.remove(j+1)
                    }
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    private fun setValues() {
        with(binding) {
            city.setText(offerViewModel.city ?: "")
            distance.setText(offerViewModel.distnace?.toString() ?: "")
            minPrice.setText(offerViewModel.minPrice?.toString() ?: "")
            maxPrice.setText(offerViewModel.maxPrice?.toString() ?: "")
            minSize.setText(offerViewModel.minArea?.toString() ?: "")
            maxSize.setText(offerViewModel.maxArea?.toString() ?: "")
            minYear.setText(offerViewModel.minYear?.toString() ?: "")
            maxYear.setText(offerViewModel.maxYear?.toString() ?: "")
            rooms.setText(offerViewModel.minNumberOfRooms?.toString() ?: "")
            floor.setText(offerViewModel.floor?.toString() ?: "")
            if(offerViewModel.propertyType != null)
                propertyTypeSpinner.setSelection(propertyTypeAdapter.getPosition(PropertyType.fromValue(offerViewModel.propertyType!!).name))
            voivodeship.setSelection(voivodeshipAdapter.getPosition(offerViewModel.province))
            val districts = realEstateViewModel.getDistricts(city.text.toString())
            if(districts.isEmpty()) {
                district.isEnabled = false
            } else {
                updateDistrictSpinner(districts)
                district.setSelection(districtAdapter.getPosition(realEstateViewModel.district))
            }
        }
    }

    private fun collectData() {
        with(binding){
            if(isNotEmpty(city))
                offerViewModel.city = city.text.toString()
            if(isNotEmpty(distance))
                offerViewModel.distnace = distance.text.toString().toInt()
            if(isNotEmpty(minPrice))
                offerViewModel.minPrice = minPrice.text.toString().toInt()
            if(isNotEmpty(maxPrice))
                offerViewModel.maxPrice = maxPrice.text.toString().toInt()
            if(isNotEmpty(minSize))
                offerViewModel.minArea = minSize.text.toString().toInt()
            if(isNotEmpty(maxSize))
                offerViewModel.maxArea = maxSize.text.toString().toInt()
            if(isNotEmpty(minYear))
                offerViewModel.minYear = minYear.text.toString().toInt()
            if(isNotEmpty(maxYear))
                offerViewModel.maxYear = maxYear.text.toString().toInt()
            if(isNotEmpty(rooms))
                offerViewModel.minNumberOfRooms = rooms.text.toString().toInt()
            if(isNotEmpty(floor))
                offerViewModel.floor = floor.text.toString().toInt()


        }
        if(selectedProperty.isNotEmpty() && selectedProperty != DEFAULT_PROPERTY_TYPE)
            offerViewModel.propertyType = PropertyType.valueOf(selectedProperty).value
        if(selectedVoivodeship.isNotEmpty())
            offerViewModel.province = selectedVoivodeship
        if(selectedDistrict.isNotEmpty())
            offerViewModel.district = selectedDistrict
        if(pickedEquipment != null && pickedEquipment.isNotEmpty())
            offerViewModel.equipment = pickedEquipment
    }

    private fun isNotEmpty(editText: EditText) : Boolean{
        return !editText.text.isNullOrEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}