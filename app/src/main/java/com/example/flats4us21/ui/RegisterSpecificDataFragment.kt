package com.example.flats4us21.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import java.util.Calendar

class RegisterSpecificDataFragment : Fragment() {
    private  var _binding: FragmentRegisterSpecificDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var selectedBirthDate : LocalDate? = null
    private var selectedExpireDate : LocalDate? = null
    private lateinit var photoAdapter: PhotoAdapter
    private var selectedImageUris : MutableList<Uri> = mutableListOf()
    private var lastIndexBeforeUpdate : Int = 0
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

        setDocumentImages()
        setVisibility()
        setValues()

        binding.layoutBirthDate.setOnClickListener {
            clickBirthDatePicker(binding.birthDate)
        }

        binding.layoutDocumentExpireDate.setOnClickListener {
            clickExpireDatePicker(binding.documentExpireDate)
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
                val remainingSpace = 2 - selectedImageUris.size
                if (remainingSpace > 0) {
                    val urisToAdd = uris.take(remainingSpace)
                    selectedImageUris.addAll(urisToAdd)

                    val startIndex = lastIndexBeforeUpdate
                    val endIndex = lastIndexBeforeUpdate + urisToAdd.size
                    photoAdapter.updateData(selectedImageUris)
                    lastIndexBeforeUpdate = endIndex
                } else {
                    Toast.makeText(requireContext(), "Możesz dodać maksymalnie 2 zdjęć!", Toast.LENGTH_SHORT).show()
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

    private fun clickBirthDatePicker(textView: TextView) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                textView.text = selectedDate.toString()
                selectedBirthDate = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun clickExpireDatePicker(textView: TextView) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                textView.text = selectedDate.toString()
                selectedExpireDate = selectedDate
            },
            year,
            month,
            day
        )
        // Ustawienie minimalnej daty na jutro
        myCalendar.add(Calendar.DAY_OF_MONTH, 1)
        datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setValues() {
        if(userViewModel.birthDate != null && userViewModel.userType == UserType.STUDENT.toString()){
            selectedBirthDate = userViewModel.birthDate
            binding.birthDate.text = userViewModel.birthDate.toString()
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
        if(userViewModel.documentNumber != "" ){
            binding.documentNumber.setText(userViewModel.documentNumber)
        }
        if(userViewModel.documentExpireDate != null){
            selectedExpireDate = userViewModel.documentExpireDate
            binding.documentExpireDate.text = userViewModel.documentExpireDate.toString()
        }
        selectedImageUris = userViewModel.images
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
            userViewModel.birthDate = selectedBirthDate
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
        if(!binding.documentNumber.text.isNullOrEmpty() && binding.layoutDocumentNumberWithHeader.isVisible){
            userViewModel.documentNumber = binding.documentNumber.text.toString()
        }
        if(selectedDocumentType != null){
            userViewModel.documentType = selectedDocumentType
        }
        if(!binding.documentExpireDate.text.isNullOrEmpty() && binding.layoutDocumentExpireDateWithHeader.isVisible){
            userViewModel.documentExpireDate = selectedExpireDate
        }
        if(selectedImageUris.size > 0){
            userViewModel.images = selectedImageUris
        }
    }

    private fun validateData(): Boolean {
        val isBirthDateValid = validateOptionalTextView(binding.birthDate, binding.layoutBirthDate, binding.layoutBirthDateHeader, binding.layoutBirthDateWithHeader)
        val isUniversityValid = validateOptionalEditText(binding.university, binding.layoutUniversity, binding.layoutUniversityHeader, binding.layoutUniversityWithHeader)
        val isStudentNumberValid = validateOptionalEditText(binding.studentNumber, binding.layoutStudentNumber, binding.layoutStudentNumberHeader, binding.layoutStudentNumberWithHeader)
        val isBankAccountValid = validateOptionalEditText(binding.bankAccount, binding.layoutBankAccount, binding.layoutBankAccountHeader, binding.layoutBankAccountWithHeader)
        val isDocumentNumberValid = validateOptionalEditText(binding.documentNumber, binding.layoutDocumentNumber, binding.layoutDocumentNumberHeader, binding.layoutDocumentNumberWithHeader)
        val isDocumentExpireDateValid = validateOptionalTextView(binding.documentExpireDate, binding.layoutDocumentExpireDate, binding.layoutDocumentExpireDateHeader, binding.layoutDocumentExpireDateWithHeader)
        val isDocumentPhotosValid = validateImages()

        return isBirthDateValid && isUniversityValid && isStudentNumberValid && isBankAccountValid && isDocumentNumberValid && isDocumentExpireDateValid && isDocumentPhotosValid
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

    private fun validateImages(): Boolean {
        if(selectedImageUris.size > 0){
            binding.warning.isVisible = false
            binding.photoRecyclerView.isVisible = true
        } else {
            binding.warning.isVisible = true
            binding.photoRecyclerView.isVisible = false
        }
        return selectedImageUris.size > 0
    }
}
