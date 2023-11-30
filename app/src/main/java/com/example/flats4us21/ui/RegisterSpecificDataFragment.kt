package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterSpecificDataBinding
import com.example.flats4us21.viewmodels.UserViewModel

class RegisterSpecificDataFragment : Fragment() {
    private  var _binding: FragmentRegisterSpecificDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

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

        setVisibility()
        setValues()

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
                val fragment = SurveyFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
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
    }

    private fun setVisibility() {
        if(userViewModel.userType.toString() == UserType.OWNER.toString()){
            binding.header.text = getString(R.string.owner_data)
            binding.layoutBirthDateWithHeader.visibility = View.GONE
            binding.layoutUniversityWithHeader.visibility = View.GONE
            binding.layoutStudentNumberWithHeader.visibility = View.GONE
            binding.layoutBankAccountWithHeader.visibility = View.VISIBLE
        } else {
            binding.header.text = getString(R.string.student_data)
            binding.layoutBankAccountWithHeader.visibility = View.GONE
            binding.layoutBirthDateWithHeader.visibility = View.VISIBLE
            binding.layoutUniversityWithHeader.visibility = View.VISIBLE
            binding.layoutStudentNumberWithHeader.visibility = View.VISIBLE
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
    }

    private fun validateData(): Boolean {
        val isBirthDateValid = validateOptionalText(binding.birthDate, binding.layoutBirthDate, binding.layoutBirthDateHeader, binding.layoutBirthDateWithHeader)
        val isUniversityValid = validateOptionalText(binding.university, binding.layoutUniversity, binding.layoutUniversityHeader, binding.layoutUniversityWithHeader)
        val isStudentNumberValid = validateOptionalText(binding.studentNumber, binding.layoutStudentNumber, binding.layoutStudentNumberHeader, binding.layoutStudentNumberWithHeader)
        val isBankAccountValid = validateOptionalText(binding.bankAccount, binding.layoutBankAccount, binding.layoutBankAccountHeader, binding.layoutBankAccountWithHeader)

        return isBirthDateValid && isUniversityValid && isStudentNumberValid && isBankAccountValid
    }

    private fun validateOptionalText(editText: EditText, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isRequired || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired || !isVisible
    }
}
