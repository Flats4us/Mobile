package com.example.flats4us21.ui

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
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
        Log.d(TAG, "Start")

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

        val nextButton = binding.nextButton
        nextButton.setOnClickListener {
            val fragment = SurveyFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
        }
        val prevButton = binding.prevButton
        prevButton.setOnClickListener {
            val fragment = RegisterSelectingUserTypeFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}