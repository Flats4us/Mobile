package com.example.flats4us21.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.databinding.FragmentAddRealEstateSecondStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import java.io.File
import java.util.*

class AddRealEstateSecondStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateSecondStepBinding? = null
    private val binding get() = _binding!!
    private var selectedConstructionYear: String = ""
    private lateinit var DEFAULT_SPINNER_VALUE: String
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var constructionYearAdapter : ArrayAdapter<String>
    private lateinit var pickedEquipment: MutableList<Int>
    private var file: File? = null
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
        setListeners()
    }

    private fun setListeners() {
        val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val fileName = getFileNameFromUri(uri)

                // Resolve the content URI to obtain a file descriptor
                val inputStream = requireContext().contentResolver.openInputStream(uri)

                // Create a temporary file in your app's cache directory
                file = createTempFile(fileName.toString(), ".tmp")

                // Copy the content of the selected file to the temporary file
                inputStream?.use { input ->
                    file?.outputStream()?.use { output ->
                        input.copyTo(output)
                    }
                }

                binding.fileName.text = fileName.toString()
                binding.fileNameLayout.isVisible = true
                binding.addRulesButton.isVisible = false
                binding.warning.isVisible = false
            }
        }
        binding.addRulesButton.setOnClickListener {
            getContent.launch(arrayOf("application/pdf", "text/plain"))
        }

        binding.prevButton.setOnClickListener {
            collectData()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
            (requireParentFragment() as AddRealEstateFragment).decreaseProgressBar()
        }
        binding.nextButton.setOnClickListener {
            validateData()
            if(test){
                collectData()
                (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateThirdStepFragment())
                (requireParentFragment() as AddRealEstateFragment).increaseProgressBar()
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
            if(realEstateViewModel.propertyType == PropertyType.HOUSE){
                if(realEstateViewModel.landArea !=0){
                    landArea.setText(realEstateViewModel.landArea.toString())
                }
                layoutLandAreaWithHeader.isVisible = true
                if(realEstateViewModel.numberOfFloors != 0)
                    numberOfFloors.setText(realEstateViewModel.numberOfFloors.toString())
                numberOfFloorsHeader.setText(R.string.required_number_of_floors)
            }
            if(realEstateViewModel.maxResidents != 0){
                maxResidents.setText(realEstateViewModel.maxResidents.toString())
            }
            constructionYearSpinner.setSelection(constructionYearAdapter.getPosition(realEstateViewModel.constructionYear.toString()))
            if(realEstateViewModel.propertyType != PropertyType.ROOM){
                if(realEstateViewModel.numberOfRooms != 0){
                    numberOfRooms.setText(realEstateViewModel.numberOfRooms.toString())
                }
                numberOfRoomsHeader.setText(R.string.required_number_of_rooms)
            } else {
                numberOfRoomsHeader.setText(R.string.number_of_rooms)
                numberOfFloorsHeader.setText(R.string.number_of_floors)
            }
            pickedEquipment.clear()
            pickedEquipment.addAll(realEstateViewModel.equipment)
            if(!realEstateViewModel.isCreating){
                addRulesButton.visibility = View.GONE
            }

        }

    }

    private fun getFileNameFromUri(uri: Uri): Any {
        var fileName = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
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
        realEstateViewModel.titleDeedFile = file
 }

    private fun validateData() {
        val isAreaValid = validateInteger(binding.area, binding.layoutArea)
        val isLandAreaValid = validateOptionalText(binding.landArea, binding.layoutLandArea, binding.landAreaHeader)
        val isMaxResidentsValid = validateInteger(binding.maxResidents, binding.layoutMaxResidents)
        val isConstructionYearValid = validateSpinner(binding.constructionYearSpinner, binding.layoutConstructionYear, selectedConstructionYear)
        val isNumberOfRoomsValid = validateOptionalText(binding.numberOfRooms, binding.layoutNumberOfRooms, binding.numberOfRoomsHeader)
        val isNumberOfFloorsValid = validateOptionalText(binding.numberOfFloors, binding.layoutNumberOfFloors, binding.numberOfFloorsHeader)
        val isFileValid = validateFile(file)
        test = isAreaValid && isLandAreaValid && isMaxResidentsValid && isConstructionYearValid && isNumberOfRoomsValid && isNumberOfFloorsValid && isFileValid

    }

    private fun validateFile(file: File?): Boolean {
        return if (binding.addRulesButton.isVisible && file == null) {
            binding.warning.isVisible = true
            false
        } else {
            binding.warning.isVisible = false
            true
        }
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

    private fun validateOptionalText(editText: EditText, editTextLayout : ViewGroup, header : TextView): Boolean {
        val text = editText.text.toString().toIntOrNull()
        val isRequired = header.text.last() == '*'
        val isValid = text != null

        editTextLayout.setBackgroundResource(if (isValid || !isRequired) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}