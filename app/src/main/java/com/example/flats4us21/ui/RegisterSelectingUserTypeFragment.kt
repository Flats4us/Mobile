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
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterSelectingUserTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        //TODO: create setup fun

        val radioGroup = binding.userTypeRadioGroup

        val initialSelection = if(userViewModel.userType != null) UserType.valueOf(userViewModel.userType!!) else UserType.STUDENT
        setRadioButtonSelection(radioGroup, initialSelection)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = binding.root.findViewById(checkedId)
            selectedUserType = selectedRadioButton.tag as UserType
        }

        binding.nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                //TODO: go to the next fragment
            }
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