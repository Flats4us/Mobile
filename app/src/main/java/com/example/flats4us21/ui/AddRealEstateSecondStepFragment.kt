package com.example.flats4us21.ui

import android.app.AlertDialog
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
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateSecondStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import java.util.*

class AddRealEstateSecondStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateSecondStepBinding? = null
    private val binding get() = _binding!!
    private var selectedConstructionYear: String = ""
    private lateinit var DEFAULT_SPINNER_VALUE: String
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var constructionYearAdapter : ArrayAdapter<String>
    private lateinit var pickedEquipment: MutableList<String>
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
        DEFAULT_SPINNER_VALUE = getString(R.string.choose_an_option)

        pickedEquipment = mutableListOf()
        setupConstructionYearSpinner()
        setValues()
        setupEquipment()

        binding.prevButton.setOnClickListener {
            collectData()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
        }
        binding.nextButton.setOnClickListener {
            validateData()
            if(test){
                collectData()
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

    private fun setupEquipment() {
        val equipment = binding.equipment
        val equipmentList: MutableList<Int> = mutableListOf()
        val equipmentArray = realEstateViewModel.getEquipmentList().map { it }.toTypedArray()
        val selectedEquipment = BooleanArray(equipmentArray.size) { index ->
            realEstateViewModel.equipment.contains(equipmentArray[index])
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
            builder.setPositiveButton("OK") { _, _ ->
                for (j in 0 until equipmentList.size) {
                    pickedEquipment.add(equipmentArray[equipmentList[j]])
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setNeutralButton("Clear All") { _, _ ->
                for (j in selectedEquipment.indices) {
                    selectedEquipment[j] = false
                    equipmentList.clear()
                    equipment.text = ""
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        adapter.insert(DEFAULT_SPINNER_VALUE, 0)
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
            constructionYearSpinner.setSelection(constructionYearAdapter.getPosition(realEstateViewModel.constructionYear.toString()))
            if(realEstateViewModel.propertyType != PropertyType.ROOM.toString()){
                if(realEstateViewModel.numberOfRooms != 0){
                    numberOfRooms.setText(realEstateViewModel.numberOfRooms.toString())
                }
                layoutNumberOfRoomsWithHeader.isVisible = true
                if(realEstateViewModel.numberOfFloors != 0){
                    numberOfFloors.setText(realEstateViewModel.numberOfFloors.toString())
                }
                layoutNumberOfFloorsWithHeader.isVisible = true
            } else {
                layoutNumberOfRoomsWithHeader.isVisible = false
                layoutNumberOfFloorsWithHeader.isVisible = false
            }
            pickedEquipment.clear()
            pickedEquipment.addAll(realEstateViewModel.equipment)

        }

    }

    private fun collectData() {
        if (!binding.area.text.isNullOrEmpty()) {
            realEstateViewModel.area = binding.area.text.toString().toInt()
        }
        if (!binding.landArea.text.isNullOrEmpty()) {
            realEstateViewModel.landArea = binding.landArea.text.toString().toInt()
        }
        if (!binding.maxResidents.text.isNullOrEmpty()) {
            realEstateViewModel.maxResidents = binding.maxResidents.text.toString().toInt()
        }
        if (selectedConstructionYear != DEFAULT_SPINNER_VALUE) {
            realEstateViewModel.constructionYear = selectedConstructionYear.toInt()
        }
        if (!binding.numberOfRooms.text.isNullOrEmpty()) {
            realEstateViewModel.numberOfRooms = binding.numberOfRooms.text.toString().toInt()
        }
        if (!binding.numberOfFloors.text.isNullOrEmpty()) {
            realEstateViewModel.numberOfFloors = binding.numberOfFloors.text.toString().toInt()
        }
        realEstateViewModel.equipment = pickedEquipment
 }

    private fun validateData() {
        val isAreaValid = validateInteger(binding.area, binding.layoutArea)
        val isLandAreaValid = validateOptionalText(binding.landArea, binding.layoutLandArea, binding.layoutLandAreaWithHeader)
        val isMaxResidentsValid = validateInteger(binding.maxResidents, binding.layoutMaxResidents)
        val isConstructionYearValid = validateSpinner(binding.constructionYearSpinner, binding.layoutConstructionYear, selectedConstructionYear)
        val isNumberOfRoomsValid = validateInteger(binding.numberOfRooms, binding.layoutNumberOfRooms)
        val isNumberOfFloorsValid = validateInteger(binding.numberOfFloors, binding.layoutNumberOfFloors)

        test = isAreaValid && isLandAreaValid && isMaxResidentsValid && isConstructionYearValid && isNumberOfRoomsValid && isNumberOfFloorsValid

    }

    private fun validateInteger(editText: EditText, editTextLayout : ViewGroup): Boolean {
        val text = editText.text.toString().toIntOrNull()
        val isValid = text != null

        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !editText.isVisible
    }

    private fun validateSpinner(spinner : Spinner, spinnerLayout : ViewGroup, selectedItem: String): Boolean {
        val isValid = selectedItem != DEFAULT_SPINNER_VALUE

        spinnerLayout.setBackgroundResource(if (isValid || !spinner.isEnabled) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !spinner.isEnabled
    }

    private fun validateOptionalText(editText: EditText, editTextLayout : ViewGroup, editTextLayoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString().toIntOrNull()
        val isValid = text != null

        editTextLayout.setBackgroundResource(if (isValid || !editTextLayoutWithHeader.isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !editTextLayoutWithHeader.isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}