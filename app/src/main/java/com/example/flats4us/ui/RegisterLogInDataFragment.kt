package com.example.flats4us.ui

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.data.UserType
import com.example.flats4us.databinding.FragmentRegisterLogInDataBinding
import com.example.flats4us.viewmodels.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val TAG = "RegisterLogInDataFragment"
class RegisterLogInDataFragment : Fragment() {
    private var _binding : FragmentRegisterLogInDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var warnings : MutableList<String> = mutableListOf()

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

        userViewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            if (resultMessage != null) {
                Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                userViewModel.clearErrorMessage()
            }
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
            validateData { valid ->
                if (valid) {
                    collectData()
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        val token = task.result
                        Log.d(TAG, "FCM Token: $token")
                        userViewModel.createUser(token) {
                            if (it) {
                                val fragment = LoginFragment()
                                (activity as? DrawerActivity)!!.replaceFragment(fragment)
                                (requireParentFragment() as RegisterParentFragment).decreaseProgressBar(
                                    100
                                )
                                userViewModel.clearData()
                            }
                        }
                    })
                } else {
                    warnings.forEach { warning ->
                        Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validateData(callback: (Boolean) -> Unit) {
        isEmailValid(binding.email, binding.layoutEmail) {
            val isEmailValid = it
            val isPasswordValid = isPasswordValid(binding.textPassword, binding.layoutPassword)
            val isRepeatPasswordValid = isPasswordValid(binding.textRepeatPassword, binding.layoutRepeatPassword)
            val arePasswordsTheSame = arePasswordsTheSame(binding.textPassword, binding.textRepeatPassword)

            callback(isEmailValid && isPasswordValid && isRepeatPasswordValid && arePasswordsTheSame)
        }
    }

    private fun isEmailValid(editText: EditText, editTextLayout: ViewGroup, callback: (Boolean) -> Unit) {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        userViewModel.checkEmail(email) { exists ->
            if(exists)
                Toast.makeText(requireContext(), getString(R.string.email_already_exist), Toast.LENGTH_LONG).show()
            val allConditionsMet = isValid && !exists
            Log.i(TAG, "isEmail: $isValid, exists: $exists, all: $allConditionsMet")
            editTextLayout.setBackgroundResource(if (allConditionsMet) R.drawable.background_input else R.drawable.background_wrong_input)
            callback(allConditionsMet)
        }
    }

    private fun isPasswordValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val password = editText.text.toString()
        val isNotEmpty = password.isNotEmpty()
        val isLengthValid = password.length in 8..50
        val pattern: Pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$")
        val matcher: Matcher = pattern.matcher(password)
        val isPasswordComplexValid = matcher.find()
        val isValid = isNotEmpty && isLengthValid && isPasswordComplexValid

        if(!isNotEmpty){
            warnings.add(getString(R.string.empty_password))
        } else if(!isLengthValid){
            warnings.add(getString(R.string.password_between_8_and_50_signs))
        }else if(!isPasswordComplexValid){
            warnings.add(getString(R.string.password_validation))
        }

        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

    private fun arePasswordsTheSame(password: EditText, repeatPassword: EditText): Boolean {
        val arePasswordsValid = password.text.toString() == repeatPassword.text.toString()
        if(!arePasswordsValid){
            warnings.add(getString(R.string.passwords_not_matching))
        }
        return arePasswordsValid
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