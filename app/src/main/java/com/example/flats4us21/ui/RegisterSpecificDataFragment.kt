package com.example.flats4us21.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flats4us21.R
import com.example.flats4us21.adapters.PhotoAdapter
import com.example.flats4us21.data.DocumentType
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterSpecificDataBinding
import com.example.flats4us21.viewmodels.UserViewModel
import java.time.LocalDate
import java.util.*

class RegisterSpecificDataFragment : Fragment() {
    private  var _binding: FragmentRegisterSpecificDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var selectedExpireDate : LocalDate? = null
    private lateinit var photoAdapter: PhotoAdapter
    private var selectedImageUris : MutableList<Uri> = mutableListOf()
    private var lastIndexBeforeUpdate : Int = 0
    private val documentTypeMap = mapOf(
        null to 0,
        DocumentType.IDENTITY to 1,
        DocumentType.STUDENT_ID to 2
    )
    private var selectedDocumentType : DocumentType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterSpecificDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()
        setDocumentImages()
        setVisibility()
        setValues()

        binding.layoutDocumentExpireDate.setOnClickListener{
            clickDatePicker()
        }

        val prevButton = binding.prevButton
        prevButton.setOnClickListener {
            collectData()
            val fragment = RegisterFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }

        val nextButton = binding.nextButton
        nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                var fragment: Fragment = SurveyFragment()
                if(userViewModel.userType == UserType.OWNER.toString())
                        fragment = RegisterLogInDataFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun setDocumentImages() {
        val multiplePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(15)) { uris ->
                val remainingSpace = 16 - selectedImageUris.size
                if (remainingSpace > 0) {
                    val urisToAdd = uris.take(remainingSpace)
                    selectedImageUris.addAll(urisToAdd)

                    val startIndex = lastIndexBeforeUpdate
                    val endIndex = lastIndexBeforeUpdate + urisToAdd.size
                    photoAdapter.notifyItemRangeInserted(startIndex, endIndex)
                    lastIndexBeforeUpdate = endIndex
                } else {
                    Toast.makeText(requireContext(), "Możesz dodać maksymalnie 16 zdjęć!", Toast.LENGTH_SHORT).show()
                }
                if(selectedImageUris.size > 0){
                    binding.warning.isVisible = false
                    binding.photoRecyclerView.isVisible = true
                }
            }

        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        photoAdapter = PhotoAdapter(selectedImageUris)
        recyclerView.adapter = photoAdapter

        binding.addPhotoButton.setOnClickListener {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun setSpinner() {
        val spinnerValues = arrayOf(getString(R.string.choose_an_option), "Dowód", "Legitymacja Studencka")

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerValues)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.documentType.adapter = spinnerAdapter
        binding.documentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedEnumValue = getEnumValueFromPosition(position)
                selectedDocumentType = selectedEnumValue
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun getEnumValueFromPosition(position: Int): DocumentType? {
        return documentTypeMap.entries.find { it.value == position }?.key
    }

    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            selectedExpireDate = LocalDate.of(selectedYear, selectedMonth, selectedDayOfMonth)
            binding.documentExpireDate.text = selectedExpireDate.toString()
        },
        year,
        month,
        day).show()
    }

    private fun setValues() {
        if(userViewModel.birthDate != "" && userViewModel.userType == UserType.STUDENT.toString()){
            binding.birthDate.setText(userViewModel.birthDate)
        }
        if(userViewModel.university != "" && userViewModel.userType == UserType.STUDENT.toString()){
            binding.university.setText(userViewModel.university)
        }
        if(userViewModel.studentNumber != "" && userViewModel.userType == UserType.STUDENT.toString()){
            binding.studentNumber.setText(userViewModel.studentNumber)
        }
        if(userViewModel.bankAccount != "" && userViewModel.userType == UserType.OWNER.toString()){
            binding.bankAccount.setText(userViewModel.bankAccount)
        }
        if(userViewModel.documentType != null){
            val positionToSelect = documentTypeMap[userViewModel.documentType]
            if(positionToSelect != null) {
                binding.documentType.setSelection(positionToSelect)
            }
        }
        if(userViewModel.documentExpireDate != null){
            selectedExpireDate = userViewModel.documentExpireDate
            binding.documentExpireDate.text = userViewModel.documentExpireDate.toString()
        }
    }

    private fun setVisibility() {
        if(userViewModel.userType.toString() == UserType.OWNER.toString()){
            binding.header.text = getString(R.string.owner_data)
            binding.layoutBirthDateWithHeader.visibility = View.GONE
            binding.layoutUniversityWithHeader.visibility = View.GONE
            binding.layoutStudentNumberWithHeader.visibility = View.GONE
            binding.layoutBankAccountWithHeader.visibility = View.VISIBLE
            binding.layoutDocumentNumberWithHeader.visibility = View.VISIBLE
        } else {
            binding.header.text = getString(R.string.student_data)
            binding.layoutBankAccountWithHeader.visibility = View.GONE
            binding.layoutBirthDateWithHeader.visibility = View.VISIBLE
            binding.layoutUniversityWithHeader.visibility = View.VISIBLE
            binding.layoutStudentNumberWithHeader.visibility = View.VISIBLE
            binding.layoutDocumentNumberWithHeader.visibility = View.GONE
        }
    }

    private fun collectData() {
        if(!binding.birthDate.text.isNullOrEmpty() && binding.layoutBirthDateWithHeader.isVisible){
            userViewModel.birthDate = binding.birthDate.text.toString()
        }
        if(!binding.university.text.isNullOrEmpty() && binding.layoutUniversityWithHeader.isVisible){
            userViewModel.university = binding.university.text.toString()
        }
        if(!binding.studentNumber.text.isNullOrEmpty() && binding.layoutStudentNumberWithHeader.isVisible){
            userViewModel.studentNumber = binding.studentNumber.text.toString()
        }
        if(!binding.bankAccount.text.isNullOrEmpty() && binding.layoutBankAccountWithHeader.isVisible){
            userViewModel.bankAccount = binding.bankAccount.text.toString()
        }
        if(selectedDocumentType != null){
            userViewModel.documentType = selectedDocumentType
        }
        if(!binding.documentExpireDate.text.isNullOrEmpty() && binding.layoutDocumentExpireDateWithHeader.isVisible){
            userViewModel.documentExpireDate = selectedExpireDate
            userViewModel.images = selectedImageUris
        }
    }

    private fun validateData(): Boolean {
        val isBirthDateValid = validateOptionalTextView(binding.birthDate, binding.layoutBirthDate, binding.layoutBirthDateHeader, binding.layoutBirthDateWithHeader)
        val isUniversityValid = validateOptionalEditText(binding.university, binding.layoutUniversity, binding.layoutUniversityHeader, binding.layoutUniversityWithHeader)
        val isStudentNumberValid = validateOptionalEditText(binding.studentNumber, binding.layoutStudentNumber, binding.layoutStudentNumberHeader, binding.layoutStudentNumberWithHeader)
        val isBankAccountValid = validateOptionalEditText(binding.bankAccount, binding.layoutBankAccount, binding.layoutBankAccountHeader, binding.layoutBankAccountWithHeader)
        val isDocumentTypeValid = validateSpinner(binding.documentType, binding.documentTypeHeader)

        return isBirthDateValid && isUniversityValid && isStudentNumberValid && isBankAccountValid && isDocumentTypeValid
    }

    private fun validateOptionalEditText(editText: EditText, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isRequired || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired || !isVisible
    }

    private fun validateOptionalTextView(textView: TextView, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = textView.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isRequired || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired || !isVisible
    }

    private fun validateSpinner(spinnerLayout: ViewGroup, header: TextView) : Boolean{
        val isValid = selectedDocumentType != null
        val isRequired = header.text.last() == '*'

        spinnerLayout.setBackgroundResource(if (isValid || !isRequired) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired
    }
}
