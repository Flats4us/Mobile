package com.example.flats4us21.ui

import android.os.Bundle
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
import com.example.flats4us21.adapters.DropdownCheckboxAdapter
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateSecondStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import java.util.*

class AddRealEstateSecondStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateSecondStepBinding? = null
    private val binding get() = _binding!!
    private var selectedConstructionYear: String = ""
    private lateinit var DEFAULT_PROPERTY_TYPE: String
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var constructionYearAdapter : ArrayAdapter<String>
    private lateinit var equipmentAdapter : DropdownCheckboxAdapter
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
        DEFAULT_PROPERTY_TYPE = getString(R.string.choose_an_option)

        setupConstructionYearSpinner()
        setupEquipmentAutoComplete()
        setValues()

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
    private fun setupConstructionYearSpinner() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearsInt = (1900..currentYear).toList().reversed()
        val yearsString = yearsInt.map { it.toString() }

        val constructionYearSpinner = binding.constructionYearSpinner
        constructionYearAdapter = createSpinnerAdapter(yearsString)
        constructionYearSpinner.adapter = constructionYearAdapter

        val onItemSelectedListener = createOnItemSelectedListener { selectedConstructionYear = it }
        constructionYearSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun setupEquipmentAutoComplete() {
        val equipmentAutoComplete = binding.equipment

        val items = realEstateViewModel.getEquipmentList()
        val selectedItems = MutableList(items.size) { false }

        equipmentAdapter = DropdownCheckboxAdapter(this, items, selectedItems)
        equipmentAutoComplete.setAdapter(equipmentAdapter)
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

    private fun setValues(){
        with(binding) {
            if(realEstateViewModel.area != 0){
                area.setText(realEstateViewModel.area.toString())
            }
            if(realEstateViewModel.propertyType == PropertyType.HOUSE.toString()){
                if(realEstateViewModel.landArea !=0){
                    landArea.setText(realEstateViewModel.landArea.toString())
                }
                layoutLandAreaWithHeader.isVisible = true
            }
            if(realEstateViewModel.maxResidents != 0){
                maxResidents.setText(realEstateViewModel.maxResidents.toString())
            }
            constructionYearSpinner.setSelection(constructionYearAdapter.getPosition(realEstateViewModel.propertyType))
            if(realEstateViewModel.numberOfRooms != 0){
                numberOfRooms.setText(realEstateViewModel.numberOfRooms.toString())
            }
            if(realEstateViewModel.numberOfFloors != 0){
                numberOfFloors.setText(realEstateViewModel.numberOfFloors.toString())
            }
            equipmentAdapter.setSelectedItems(List(realEstateViewModel.equipment.size) { realEstateViewModel.equipment.contains(realEstateViewModel.getEquipmentList()[it]) })
        }

    }

    private fun collectData() {
        val isAreaValid = setAndValidateInteger(binding.area, binding.layoutArea) { value ->
            realEstateViewModel.area = value
        }
        val isLandAreaValid = setAndValidateOptionalText(binding.landArea, binding.layoutLandArea, binding.layoutLandAreaWithHeader) { value ->
            realEstateViewModel.landArea = value
        }
        val isMaxResidentsValid = setAndValidateInteger(binding.maxResidents, binding.layoutMaxResidents) { value ->
            if (value != null) {
                realEstateViewModel.maxResidents = value
            }
        }
        val isConstructionYearValid = setSelectedItemAndValidate(binding.constructionYearSpinner, binding.layoutConstructionYear, selectedConstructionYear) { value ->
            realEstateViewModel.propertyType = value
        }
        val isNumberOfRoomsValid = setAndValidateInteger(binding.numberOfRooms, binding.layoutNumberOfRooms) { value ->
            realEstateViewModel.numberOfRooms = value
        }
        val isNumberOfFloorsValid = setAndValidateInteger(binding.numberOfFloors, binding.layoutNumberOfFloors) { value ->
            if (value != null) {
                realEstateViewModel.numberOfFloors = value
            }
        }
        realEstateViewModel.equipment = equipmentAdapter.getSelectedItems()

        test = isAreaValid && isLandAreaValid && isMaxResidentsValid && isConstructionYearValid && isNumberOfRoomsValid && isNumberOfFloorsValid
    }

    private fun setAndValidateInteger(editText: EditText, editTextLayout : ViewGroup, targetProperty: (Int?) -> Unit): Boolean {
        val text = editText.text.toString().toIntOrNull()
        val isValid = text != null
        if (isValid) {
            targetProperty(text)
        }
        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !editText.isVisible
    }

    private fun setSelectedItemAndValidate(spinner : Spinner, spinnerLayout : ViewGroup, selectedItem: String, targetProperty: (String) -> Unit): Boolean {
        val isValid = selectedItem != DEFAULT_PROPERTY_TYPE
        if (isValid) {
            targetProperty(selectedItem)
        }
        spinnerLayout.setBackgroundResource(if (isValid || !spinner.isEnabled) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !spinner.isEnabled
    }

    private fun setAndValidateOptionalText(editText: EditText, editTextLayout : ViewGroup, editTextLayoutWithHeader : ViewGroup, targetProperty: (Int?) -> Unit): Boolean {
        val text = editText.text.toString().toIntOrNull()
        val isValid = text != null
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