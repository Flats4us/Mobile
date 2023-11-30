package com.example.flats4us21.ui

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentRegisterBinding
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()

        val toggle = binding.passwordToggle
        val password =  binding.textPassword
        toggle.setOnClickListener{
            setPasswordVisibility(toggle, password)
        }

        val repeatToggle = binding.passwordRepeatToggle
        val repeatPassword = binding.textRepeatPassword
        repeatToggle.setOnClickListener{
            setPasswordVisibility(repeatToggle, repeatPassword)
        }

        val prevButton = binding.prevButton
        prevButton.setOnClickListener {
            collectData()
            val fragment = RegisterSelectingUserTypeFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }

        val nextButton = binding.nextButton
        nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                val fragment = RegisterSpecificDataFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun setValues() {
        if(userViewModel.name != ""){
            binding.name.setText(userViewModel.name)
        }
        if(userViewModel.surname != ""){
            binding.surname.setText(userViewModel.surname)
        }
        if(userViewModel.city != ""){
            binding.city.setText(userViewModel.city)
        }
        if(userViewModel.street != ""){
            binding.street.setText(userViewModel.street)
        }
        if(userViewModel.buildingNumber != ""){
            binding.buildingNumber.setText(userViewModel.buildingNumber)
        }
        if(userViewModel.flatNumber != ""){
            binding.flatNumber.setText(userViewModel.flatNumber)
        }
        if(userViewModel.postalCode != ""){
            binding.postalCode.setText(userViewModel.postalCode)
        }
        if(userViewModel.phoneNumber != ""){
            binding.phoneNumber.setText(userViewModel.phoneNumber)
        }
    }

    private fun setPasswordVisibility(toggle : ImageButton, password : EditText){
        if(password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun collectData() {
        if(!binding.name.text.isNullOrEmpty()) {
            userViewModel.name = binding.name.text.toString()
        }
        if(!binding.surname.text.isNullOrEmpty()){
            userViewModel.surname = binding.surname.text.toString()
        }
        if(!binding.city.text.isNullOrEmpty()){
            userViewModel.city = binding.city.text.toString()
        }
        if(!binding.street.text.isNullOrEmpty()){
            userViewModel.street = binding.street.text.toString()
        }
        if(!binding.buildingNumber.text.isNullOrEmpty()){
            userViewModel.buildingNumber = binding.buildingNumber.text.toString()
        }
        if(!binding.flatNumber.text.isNullOrEmpty()){
            userViewModel.flatNumber = binding.flatNumber.text.toString()
        }
        if(!binding.postalCode.text.isNullOrEmpty()){
            userViewModel.postalCode = binding.postalCode.text.toString()
        }
        if(!binding.phoneNumber.text.isNullOrEmpty()){
            userViewModel.phoneNumber = binding.phoneNumber.text.toString()
        }
    }

    private fun validateData(): Boolean {
        val isNameValid = validateOptionalText(binding.name, binding.layoutName, binding.layoutNameHeader)
        val isSurnameValid = validateOptionalText(binding.surname, binding.layoutSurname, binding.layoutSurnameHeader)
        val isCityValid = validateOptionalText(binding.city, binding.layoutCity, binding.cityHeader)
        val isStreetValid = validateOptionalText(binding.street, binding.layoutStreet, binding.streetHeader)
        val isBuildingNumberValid = validateOptionalText(binding.buildingNumber, binding.layoutBuildingNumber, binding.buildingNumberHeader)
        val isFlatNumberValid = validateOptionalText(binding.flatNumber, binding.layoutFlatNumber, binding.flatNumberHeader)
        val isPostalCodeValid = validateOptionalText(binding.postalCode, binding.layoutPostalCode, binding.postalCodeHeader)
        val isPhoneNumberValid = validateOptionalText(binding.phoneNumber, binding.layoutPhoneNumber, binding.phoneNumberHeader)

        return isNameValid && isSurnameValid && isCityValid && isStreetValid && isBuildingNumberValid && isFlatNumberValid && isPostalCodeValid && isPhoneNumberValid
    }

    private fun validateOptionalText(editText: EditText, editTextLayout : ViewGroup, header : TextView): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        editTextLayout.setBackgroundResource(if (isValid || !isRequired) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
