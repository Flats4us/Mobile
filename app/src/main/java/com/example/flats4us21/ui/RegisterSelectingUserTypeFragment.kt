package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterSelectingUserTypeBinding
import com.example.flats4us21.viewmodels.UserViewModel


private const val TAG = "RegisterSelectingUserTypeFragment"

class RegisterSelectingUserTypeFragment : Fragment() {
    private var _binding : FragmentRegisterSelectingUserTypeBinding? = null
    private val binding get() = _binding!!
    private var selectedUserType : UserType? = null
    private lateinit var radioGroup: RadioGroup
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterSelectingUserTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = binding.userTypeRadioGroup

        setValues()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = binding.root.findViewById(checkedId)
            selectedUserType = UserType.valueOf(selectedRadioButton.tag as String)
        }

        binding.nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(RegisterFragment())
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun setValues() {
        val initialSelection = if(userViewModel.userType != null) UserType.valueOf(userViewModel.userType!!) else null
        if(initialSelection != null){
            setRadioButtonSelection(radioGroup, initialSelection)
            selectedUserType = initialSelection
        }
    }

    private fun collectData() {
        userViewModel.userType = selectedUserType.toString()
    }

    private fun validateData(): Boolean {
        return if(selectedUserType != null){
            binding.warning.visibility = View.GONE
            true
        } else {
            binding.warning.visibility = View.VISIBLE
            false
        }
    }

    private fun setRadioButtonSelection(radioGroup: RadioGroup, value: UserType) {
        val radioButtonId = when (value) {
            UserType.STUDENT -> R.id.studentRadioButton
            UserType.OWNER -> R.id.ownerRadioButton
        }
        radioGroup.check(radioButtonId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}