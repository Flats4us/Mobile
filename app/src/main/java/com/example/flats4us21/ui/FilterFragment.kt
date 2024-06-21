package com.example.flats4us21.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentFilterBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import com.example.flats4us21.viewmodels.RealEstateViewModel

private const val TAG = "FilterFragment"

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var offerViewModel: OfferViewModel
    private var selectedProperty: String = ""
    private var selectedVoivodeship: String = ""
    private var selectedCity: String = ""
    private var selectedDistrict: String = ""
    private var selectedSorting: String = ""
    private lateinit var pickedEquipment: MutableList<Int>
    private lateinit var DEFAULT_PROPERTY_TYPE: String
    private lateinit var sortingAdapter: ArrayAdapter<String>
    private lateinit var propertyTypeAdapter: ArrayAdapter<String>
    private lateinit var voivodeshipAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var districtAdapter: ArrayAdapter<String>

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
        realEstateViewModel.loadCities(requireContext())
        realEstateViewModel.fetchVoivodeships()

        setupSortingSpinner()
        setupVoivodeshipSpinner()
        setupPropertyTypeSpinner()
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
            offerViewModel.pageNumber = 1
            val fragment = SearchFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    private fun setupSortingSpinner() {
        val sortingSpinner: Spinner = binding.sorting
        val res = resources
        val sortingOptions = res.getStringArray(R.array.sorting_options)
        sortingAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortingOptions)
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sortingSpinner.adapter = sortingAdapter

        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val sortingOption = sortingOptions[position]
                selectedSorting = if (sortingOption != DEFAULT_PROPERTY_TYPE) {
                    sortingOption
                } else {
                    ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupVoivodeshipSpinner() {
        val voivodeshipSpinner = binding.voivodeship
        val voivodeships = realEstateViewModel.voivodeshipSuggestions
        if (voivodeships.isNotEmpty() && voivodeships[0] != DEFAULT_PROPERTY_TYPE) {
            voivodeships.add(0, DEFAULT_PROPERTY_TYPE)
        }
        Log.d(TAG, "setupVoivodeshipSpinner: $voivodeships")
        voivodeshipAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, voivodeships)
        voivodeshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        voivodeshipSpinner.adapter = voivodeshipAdapter

        voivodeshipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val newSelectedVoivodeship = parent.getItemAtPosition(position) as String
                if (newSelectedVoivodeship != selectedVoivodeship) {
                    selectedVoivodeship = newSelectedVoivodeship
                    Log.d(TAG, "onItemSelected: $selectedVoivodeship")
                    setupCitySpinner(selectedVoivodeship)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCitySpinner(voivodeship: String, fetchedCity: String = "") {
        Log.d(TAG, "setupCitySpinner: $voivodeship, $fetchedCity")
        val citySpinner = binding.city
        val cities = ArrayList(realEstateViewModel.getCitiesByVoivodeship(voivodeship))
        if (cities.isNotEmpty() && cities[0] != DEFAULT_PROPERTY_TYPE) {
            cities.add(0, DEFAULT_PROPERTY_TYPE)
        }
        Log.d(TAG, "setupCitySpinner: $cities")
        cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val newSelectedCity = cities[position]
                if (newSelectedCity != selectedCity) {
                    selectedCity = if (newSelectedCity != DEFAULT_PROPERTY_TYPE) {
                        newSelectedCity
                    } else {
                        ""
                    }
                    if (newSelectedCity != DEFAULT_PROPERTY_TYPE)
                        setupDistrictSpinner(selectedCity)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        Log.d(TAG, "fetchedCity != \"\": ${fetchedCity != ""}")
        if (fetchedCity != "")
            citySpinner.setSelection(cityAdapter.getPosition(fetchedCity))
    }

    private fun setupDistrictSpinner(city: String, fetchedDistrict: String = "") {
        Log.d(TAG, "setupDistrictSpinner: $city, $fetchedDistrict")
        val districtSpinner = binding.district
        val districts = realEstateViewModel.getDistricts(city)
        if (districts.isNotEmpty() && districts[0] != DEFAULT_PROPERTY_TYPE) {
            districts.add(0, DEFAULT_PROPERTY_TYPE)
        }
        districtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, districts)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        districtSpinner.adapter = districtAdapter
        districtSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val newSelectedDistrict = districts[position]
                if (newSelectedDistrict != selectedDistrict) {
                    selectedDistrict = if (newSelectedDistrict != DEFAULT_PROPERTY_TYPE) {
                        newSelectedDistrict
                    } else {
                        ""
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        Log.d(TAG, "fetchedDistrict != \"\": ${fetchedDistrict != ""}")
        if (fetchedDistrict != "") {
            districtSpinner.setSelection(districtAdapter.getPosition(fetchedDistrict))
        }
    }

    private fun setupPropertyTypeSpinner() {
        val propertyTypeSpinner = binding.propertyTypeSpinner
        propertyTypeAdapter = createSpinnerAdapter(PropertyType.values().map { it.name })
        propertyTypeSpinner.adapter = propertyTypeAdapter

        val onItemSelectedListener = createOnItemSelectedListener {
            selectedProperty = if (it != DEFAULT_PROPERTY_TYPE) {
                it
            } else {
                ""
            }
        }
        propertyTypeSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun createOnItemSelectedListener(action: (String) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action(parent?.getItemAtPosition(position) as String)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        if (data.isNotEmpty() && data[0] != DEFAULT_PROPERTY_TYPE)
            adapter.insert(DEFAULT_PROPERTY_TYPE, 0)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun setupEquipment() {
        val equipment = binding.equipment
        val equipmentList: MutableList<Int> = mutableListOf()
        realEstateViewModel.getEquipmentList()
        realEstateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        realEstateViewModel.equipments.observe(viewLifecycleOwner) { equipments ->
            val equipmentArray = equipments.map { it.equipmentName }.toTypedArray()
            val selectedEquipment = BooleanArray(equipmentArray.size) { index ->
                realEstateViewModel.equipment.contains(index + 1)
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
                        pickedEquipment.remove(j + 1)
                    }
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    private fun setValues() {
        with(binding) {
            distance.setText(offerViewModel.distance?.toString() ?: "")
            minPrice.setText(offerViewModel.minPrice?.toString() ?: "")
            maxPrice.setText(offerViewModel.maxPrice?.toString() ?: "")
            minSize.setText(offerViewModel.minArea?.toString() ?: "")
            maxSize.setText(offerViewModel.maxArea?.toString() ?: "")
            minYear.setText(offerViewModel.minYear?.toString() ?: "")
            maxYear.setText(offerViewModel.maxYear?.toString() ?: "")
            rooms.setText(offerViewModel.minNumberOfRooms?.toString() ?: "")
            floor.setText(offerViewModel.floor?.toString() ?: "")
            if (!offerViewModel.sorting.isNullOrEmpty())
                sorting.setSelection(sortingAdapter.getPosition(QuestionTranslator.convertToSort(offerViewModel.sorting!!, requireContext())))
            if (offerViewModel.propertyType != null)
                propertyTypeSpinner.setSelection(propertyTypeAdapter.getPosition(PropertyType.fromValue(offerViewModel.propertyType!!).name))
            if (!offerViewModel.province.isNullOrEmpty()) {
                voivodeship.setSelection(voivodeshipAdapter.getPosition(offerViewModel.province), true)
                Log.d(TAG, "setValues: ${offerViewModel.province}")
                if (offerViewModel.city != null) {
                    setupCitySpinner(offerViewModel.province!!, offerViewModel.city!!)
                    Log.d(TAG, "setValues: ${offerViewModel.province}, ${offerViewModel.city}")
                    if (offerViewModel.district != null) {
                        setupDistrictSpinner(offerViewModel.city!!, offerViewModel.district!!)
                        Log.d(TAG, "setValues: ${offerViewModel.city}, ${offerViewModel.district!!}")
                    }
                }
            }
        }
    }

    private fun collectData() {
        with(binding) {
            if (isNotEmpty(distance))
                offerViewModel.distance = distance.text.toString().toInt()
            else
                offerViewModel.distance = null
            if (isNotEmpty(minPrice))
                offerViewModel.minPrice = minPrice.text.toString().toInt()
            else
                offerViewModel.minPrice = null
            if (isNotEmpty(maxPrice))
                offerViewModel.maxPrice = maxPrice.text.toString().toInt()
            else
                offerViewModel.maxPrice = null
            if (isNotEmpty(minSize))
                offerViewModel.minArea = minSize.text.toString().toInt()
            else
                offerViewModel.minArea = null
            if (isNotEmpty(maxSize))
                offerViewModel.maxArea = maxSize.text.toString().toInt()
            else
                offerViewModel.maxArea = null
            if (isNotEmpty(minYear))
                offerViewModel.minYear = minYear.text.toString().toInt()
            else
                offerViewModel.minYear = null
            if (isNotEmpty(maxYear))
                offerViewModel.maxYear = maxYear.text.toString().toInt()
            else
                offerViewModel.maxYear = null
            if (isNotEmpty(rooms))
                offerViewModel.minNumberOfRooms = rooms.text.toString().toInt()
            else
                offerViewModel.minNumberOfRooms = null
            if (isNotEmpty(floor))
                offerViewModel.floor = floor.text.toString().toInt()
            else
                offerViewModel.floor = null
        }
        if (selectedSorting.isNotEmpty())
            offerViewModel.sorting = QuestionTranslator.convertToUrl(selectedSorting, requireContext())
        else
            offerViewModel.sorting = null
        if (selectedProperty.isNotEmpty() && selectedProperty != DEFAULT_PROPERTY_TYPE)
            offerViewModel.propertyType = PropertyType.valueOf(selectedProperty).value
        else
            offerViewModel.propertyType = null
        if (selectedVoivodeship.isNotEmpty())
            offerViewModel.province = selectedVoivodeship
        else
            offerViewModel.province = null
        if (selectedCity.isNotEmpty())
            offerViewModel.city = selectedCity
        else
            offerViewModel.city = null
        if (selectedDistrict.isNotEmpty())
            offerViewModel.district = selectedDistrict
        else
            offerViewModel.district = null
        if (pickedEquipment != null && pickedEquipment.isNotEmpty())
            offerViewModel.equipment = pickedEquipment
        else
            offerViewModel.equipment = null
    }

    private fun isNotEmpty(editText: EditText): Boolean {
        return !editText.text.isNullOrEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}