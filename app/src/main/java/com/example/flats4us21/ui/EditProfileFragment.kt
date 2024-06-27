package com.example.flats4us21.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.R
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.dto.NewPasswordDto
import com.example.flats4us21.databinding.FragmentEditProfileBinding
import com.example.flats4us21.viewmodels.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val socialMediaLinks = mutableListOf<String>()
    private lateinit var userViewModel: UserViewModel
    private var selectedExpireDate : LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getMyProfile()
        setVisibility()

        userViewModel.myProfile.observe(viewLifecycleOwner) { userProfile ->
            if(userProfile != null)
                bindData(userProfile)
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.layoutSocialMediaLinks.setOnClickListener{
            val linksDialogFragment = LinksDialogFragment(socialMediaLinks)
            linksDialogFragment.show(parentFragmentManager , "LinksDialogFragment")
        }

        binding.layoutDocumentExpireDate.setOnClickListener{
            selectedExpireDate = clickDatePicker(binding.documentExpireDate)
        }

        binding.editProfileButton.setOnClickListener {
            if(validateData()){
                collectData()
                userViewModel.updateMyProfile {
                    if(it){
                        Toast.makeText(requireContext(),
                            getString(R.string.sucessfully_changed_data), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.oldPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.oldPasswordToggle, binding.oldPassword)
        }

        binding.newPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.newPasswordToggle, binding.newPassword)
        }

        binding.confirmPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.confirmPasswordToggle, binding.confirmNewPassword)
        }

        binding.editPasswordButton.setOnClickListener {
            if(validateDataForPassword()){
                collectDataForPassword()
                userViewModel.changePassword {
                    if(it) {
                        Toast.makeText(requireContext(), getString(R.string.password_changed), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.invalid_data), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun collectDataForPassword() {
        val newPasswordDto = NewPasswordDto(
            binding.oldPassword.text.toString(),
            binding.newPassword.text.toString()
        )
        userViewModel.newPassword = newPasswordDto
    }

    private fun setVisibility() {
        if(DataStoreManager.userRole.value == "Student") {
            binding.layoutDocumentNumberWithHeader.visibility = View.GONE
            binding.layoutDocumentExpireDateWithHeader.visibility = View.GONE
            binding.layoutBankAccountWithHeader.visibility = View.GONE
            binding.layoutStudentNumberWithHeader.visibility = View.VISIBLE
            binding.layoutUniversityNameWithHeader.visibility = View.VISIBLE
        } else {
            binding.layoutDocumentNumberWithHeader.visibility = View.VISIBLE
            binding.layoutDocumentExpireDateWithHeader.visibility = View.VISIBLE
            binding.layoutBankAccountWithHeader.visibility = View.VISIBLE
            binding.layoutStudentNumberWithHeader.visibility = View.GONE
            binding.layoutUniversityNameWithHeader.visibility = View.GONE
        }

    }

    private fun setPasswordVisibility(toggle : ImageButton, password : EditText) {
        if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun bindData(userProfile: MyProfile) {
        val datePattern = "yyyy-MM-dd"
        binding.textResidentialAddress.setText(userProfile.address)
        binding.phoneNumber.setText(userProfile.phoneNumber)
        if(userProfile.links != null){
            socialMediaLinks.addAll(userProfile.links)
        }
        if(DataStoreManager.userRole.value == "Student") {
            binding.textStudentNumber.setText(userProfile.studentNumber)
            binding.textUniversityName.setText(userProfile.university)
        } else {
            binding.documentNumber.setText(userProfile.documentNumber)
            binding.documentExpireDate.text = userProfile.documentExpireDate.split("T")[0]
            selectedExpireDate = stringToLocalDate(userProfile.documentExpireDate.split("T")[0], datePattern)
            binding.bankAccount.setText(userProfile.bankAccount)
        }
    }
    private fun clickDatePicker(textView: TextView) : LocalDate? {
        var selectedDate : LocalDate? = LocalDate.now()
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            selectedDate = LocalDate.of(selectedYear, selectedMonth+1, selectedDayOfMonth)
            textView.text = selectedDate.toString()
        },
            year,
            month,
            day).show()
        return selectedDate
    }

    private fun stringToLocalDate(dateString: String, pattern: String): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }

    private fun collectData() {
        if(!binding.textResidentialAddress.text.isNullOrEmpty()) {
            userViewModel.address = binding.textResidentialAddress.text.toString()
        }
        if(!binding.phoneNumber.text.isNullOrEmpty()) {
            userViewModel.phoneNumber = binding.phoneNumber.text.toString()
        }
        userViewModel.links = socialMediaLinks
        if(!binding.textStudentNumber.text.isNullOrEmpty()){
            userViewModel.studentNumber = binding.textStudentNumber.text.toString()
        }
        if(!binding.textStudentNumber.text.isNullOrEmpty()){
            userViewModel.studentNumber = binding.textStudentNumber.text.toString()
        }
        if(!binding.textUniversityName.text.isNullOrEmpty()){
            userViewModel.university = binding.textUniversityName.text.toString()
        }
        if(!binding.documentNumber.text.isNullOrEmpty()){
            userViewModel.documentNumber = binding.documentNumber.text.toString()
        }
        if(!binding.documentExpireDate.text.isNullOrEmpty()){
            userViewModel.documentExpireDate = selectedExpireDate
        }
    }

    fun validateData() : Boolean {
        val isAddressValid = validateOptionalEditText(binding.textResidentialAddress, binding.layoutResidentialAddress, binding.layoutResidentialAddressWithHeader)
        val isPhoneNumberValid = validateOptionalEditText(binding.phoneNumber, binding.layoutPhoneNumber, binding.layoutPhoneNumberWithHeader)
        val isStudentNumberValid = validateOptionalEditText(binding.textStudentNumber, binding.layoutStudentNumber, binding.layoutStudentNumberWithHeader)
        val isUniversityNameValid = validateOptionalEditText(binding.textUniversityName, binding.layoutUniversityName, binding.layoutUniversityNameWithHeader)
        val isDocumentNumberValid = validateOptionalEditText(binding.documentNumber, binding.layoutDocumentNumber, binding.layoutDocumentNumberWithHeader)
        val isDocumentExpireDateValid = validateOptionalTextView(binding.documentExpireDate, binding.layoutDocumentExpireDate, binding.layoutDocumentExpireDateWithHeader)
        val isBankAccountValid = validateOptionalEditText(binding.bankAccount, binding.layoutBankAccount, binding.layoutBankAccountWithHeader)

        return isAddressValid && isPhoneNumberValid && isStudentNumberValid && isUniversityNameValid && isDocumentNumberValid && isDocumentExpireDateValid && isBankAccountValid
    }

    private fun validateDataForPassword() : Boolean {
        val isOldPasswordValid = binding.oldPassword.text.isNotEmpty()
        val isNewPasswordValid = binding.newPassword.text.isNotEmpty()
        val isConfirmPasswordValid = binding.confirmNewPassword.text.isNotEmpty()
        val arePasswordsTheSame = arePasswordsTheSame(binding.newPassword, binding.confirmNewPassword)

        return isOldPasswordValid && isNewPasswordValid && isConfirmPasswordValid && arePasswordsTheSame
    }

    private fun validateOptionalEditText(editText: EditText, editTextLayout : ViewGroup, layoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString()
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isVisible
    }

    private fun validateOptionalTextView(textView: TextView, editTextLayout : ViewGroup, layoutWithHeader : ViewGroup): Boolean {
        val text = textView.text.toString()
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isVisible
    }

    private fun arePasswordsTheSame(password: EditText, repeatPassword: EditText): Boolean {
        val arePasswordsValid = password.text.toString() == repeatPassword.text.toString()
        if(!arePasswordsValid){
            Toast.makeText(requireContext(), "Podane hasła różnią się", Toast.LENGTH_LONG).show()
        }
        return arePasswordsValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
