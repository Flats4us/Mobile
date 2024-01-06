package com.example.flats4us21.ui

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterLogInDataBinding
import com.example.flats4us21.viewmodels.UserViewModel

class RegisterLogInDataFragment : Fragment() {
    private var _binding : FragmentRegisterLogInDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterLogInDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        binding.prevButton.setOnClickListener {
            collectData()
            var fragment: Fragment = SurveyFragment()
            if(userViewModel.userType == UserType.OWNER.toString())
                fragment = RegisterSpecificDataFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }
        binding.buttonRegister.setOnClickListener {
            if(validateData()){
                collectData()
                userViewModel.createUser()
                userViewModel.isLoading.observe(viewLifecycleOwner){ isLoading: Boolean ->
                    if(!isLoading && userViewModel.errorMessage.value == null){
                        Toast.makeText(requireContext(), "Utworzono konto", Toast.LENGTH_SHORT).show()
                        val fragment = LoginFragment()
                        (activity as? DrawerActivity)!!.replaceFragment(fragment)
                        (requireParentFragment() as RegisterParentFragment).decreaseProgressBar(100)
                        userViewModel.clearData()
                    }
                }
            }
        }
    }

    private fun validateData(): Boolean {
        val isEmailValid = isEmailValid(binding.email, binding.layoutEmail)
        val isPasswordValid = isPasswordValid(binding.textPassword, binding.layoutPassword)
        val isRepeatPasswordValid = isPasswordValid(binding.textRepeatPassword, binding.layoutRepeatPassword)
        val arePasswordsTheSame = arePasswordsTheSame(binding.textPassword, binding.textRepeatPassword)

        return isEmailValid && isPasswordValid && isRepeatPasswordValid && arePasswordsTheSame
    }

    private fun isEmailValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

    private fun isPasswordValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val isValid = editText.text.toString().isNotEmpty()
        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

    private fun arePasswordsTheSame(password: EditText, repeatPassword: EditText): Boolean {
        return password.text.toString() == repeatPassword.text.toString()
    }

    private fun collectData() {
        if(!binding.email.text.isNullOrEmpty()){
            userViewModel.email = binding.email.text.toString()
        }
        if(!binding.textPassword.text.isNullOrEmpty()){
            userViewModel.password = binding.textPassword.text.toString()
        }
        if(!binding.textRepeatPassword.text.isNullOrEmpty()){
            userViewModel.repeatPassword = binding.textRepeatPassword.text.toString()
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