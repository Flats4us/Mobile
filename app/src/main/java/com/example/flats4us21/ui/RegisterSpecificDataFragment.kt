package com.example.flats4us21.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.DocumentType
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterSpecificDataBinding
import com.example.flats4us21.viewmodels.UserViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.util.Calendar

class RegisterSpecificDataFragment : Fragment() {
    private  var _binding: FragmentRegisterSpecificDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var selectedDocument: String = ""
    private var selectedBirthDate : LocalDate? = null
    private var selectedExpireDate : LocalDate? = null
    private var uri : Uri? = null
    private var lastIndexBeforeUpdate : Int = 0
    private lateinit var propertyTypeAdapter : ArrayAdapter<String>
    private lateinit var DEFAULT_PROPERTY_TYPE: String

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
        DEFAULT_PROPERTY_TYPE = getString(R.string.choose_an_option)
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val galleryUri = it
            try{
                binding.document.setImageURI(galleryUri)
                uri = galleryUri
            }catch(e:Exception){
                e.printStackTrace()
            }
        }

        setVisibility()
        setupPropertyTypeSpinner()
        setValues()

        binding.layoutBirthDate.setOnClickListener {
            clickBirthDatePicker(binding.birthDate)
        }

        binding.layoutDocumentExpireDate.setOnClickListener {
            clickExpireDatePicker(binding.documentExpireDate)
        }

        binding.addPhotoButton.setOnClickListener {
            getImageFromGallery(galleryLauncher)
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

    private fun setupPropertyTypeSpinner() {
        val propertyTypeSpinner = binding.documentSpinner
        val documentArray = if (userViewModel.userType.toString() == UserType.OWNER.toString()) {
            DocumentType.getOwnerDocuments()
        } else {
            DocumentType.getStudentDocuments()
        }
        propertyTypeAdapter = createSpinnerAdapter(documentArray.map { it.toLocalizedString(requireContext()) })
        propertyTypeSpinner.adapter = propertyTypeAdapter

        val onItemSelectedListener = createOnItemSelectedListener {
            selectedDocument = it
        }
        propertyTypeSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        if(data.isNotEmpty() && data[0] !=DEFAULT_PROPERTY_TYPE)
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

    private fun getImageFromGallery(galleryLauncher: ActivityResultLauncher<String>) {
        galleryLauncher.launch("image/*")
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
        myCalendar.add(Calendar.DAY_OF_MONTH, 1)
        datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setValues() {
        if (userViewModel.documentType != null) {
            binding.documentSpinner.setSelection(propertyTypeAdapter.getPosition(userViewModel.documentType?.toLocalizedString(requireContext())))
            selectedDocument = userViewModel.documentType!!.toLocalizedString(requireContext())
        }
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
        if(userViewModel.images != null) {
            binding.document.setImageURI(userViewModel.images)
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
        if (!selectedDocument.isNullOrEmpty() && selectedDocument != DEFAULT_PROPERTY_TYPE) {
            userViewModel.documentType = DocumentType.fromLocalizedString(requireContext(), selectedDocument)
        }
        if(!binding.documentNumber.text.isNullOrEmpty() && binding.layoutDocumentNumberWithHeader.isVisible){
            userViewModel.documentNumber = binding.documentNumber.text.toString()
        }
        if(!binding.documentExpireDate.text.isNullOrEmpty() && binding.layoutDocumentExpireDateWithHeader.isVisible){
            userViewModel.documentExpireDate = selectedExpireDate
        }
        if(uri != null) {
            userViewModel.images = uri
            val inputStream: InputStream? =
                requireContext().contentResolver.openInputStream(uri!!)
            val mimeType = requireContext().contentResolver.getType(uri!!)
            val fileExtension = when (mimeType) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                else -> ".jpg"
            }
            val file = File.createTempFile("temp_image", fileExtension, requireContext().cacheDir)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            userViewModel.imageFile = file
        }
    }

    private fun validateData(): Boolean {
        val isBirthDateValid = validateOptionalTextView(binding.birthDate, binding.layoutBirthDate, binding.layoutBirthDateHeader, binding.layoutBirthDateWithHeader)
        val isUniversityValid = validateOptionalEditText(binding.university, binding.layoutUniversity, binding.layoutUniversityHeader, binding.layoutUniversityWithHeader)
        val isStudentNumberValid = validateOptionalEditText(binding.studentNumber, binding.layoutStudentNumber, binding.layoutStudentNumberHeader, binding.layoutStudentNumberWithHeader)
        val isBankAccountValid = validateOptionalEditText(binding.bankAccount, binding.layoutBankAccount, binding.layoutBankAccountHeader, binding.layoutBankAccountWithHeader)
        val isDocumentTypeValid = validateSelectedItem(binding.documentSpinner, selectedDocument, binding.documentTypeHeader)
        val isDocumentNumberValid = validateOptionalEditText(binding.documentNumber, binding.layoutDocumentNumber, binding.layoutDocumentNumberHeader, binding.layoutDocumentNumberWithHeader)
        val isDocumentExpireDateValid = validateOptionalTextView(binding.documentExpireDate, binding.layoutDocumentExpireDate, binding.layoutDocumentExpireDateHeader, binding.layoutDocumentExpireDateWithHeader)

        return isBirthDateValid && isUniversityValid && isStudentNumberValid && isBankAccountValid && isDocumentTypeValid && isDocumentNumberValid && isDocumentExpireDateValid
    }

    private fun validateSelectedItem(
        spinnerLayout: ViewGroup,
        selectedItem: String,
        header: TextView
    ): Boolean {
        val isValid = selectedItem != DEFAULT_PROPERTY_TYPE
        val isRequired = header.text.last() == '*'
        spinnerLayout.setBackgroundResource(if (isValid || !isRequired) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired
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

}
