package com.example.flats4us21.ui

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentLoginBinding
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.passwordToggle.setOnClickListener {
            togglePasswordVisibility(binding.passwordToggle, binding.textPassword)
        }

        binding.buttonLogin.setOnClickListener {
            if (validateData()) {
                userViewModel.login(binding.email.text.toString(), binding.textPassword.text.toString()){ success ->
                    if (success) {
                        (activity as? DrawerActivity)?.replaceFragment(SearchFragment())
                    }
                }
            }
        }

        binding.forgotYourPassword.setOnClickListener {
            (activity as? DrawerActivity)?.replaceFragment(ForgotMyPasswordFragment())
        }
    }

    private fun observeViewModel() {
        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun togglePasswordVisibility(toggle: ImageButton, password: EditText) {
        if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun validateData(): Boolean {
        val isEmailValid = validateEmail(binding.email)
        val isPasswordValid = validatePassword(binding.textPassword)

        return isEmailValid && isPasswordValid
    }

    private fun validateEmail(editText: EditText): Boolean {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isValid) {
            Toast.makeText(requireContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show()
            binding.layoutEmail.setBackgroundResource(R.drawable.background_wrong_input)
        } else {
            binding.layoutEmail.setBackgroundResource(R.drawable.background_input)
        }
        return isValid
    }

    private fun validatePassword(editText: EditText): Boolean {
        val isValid = editText.text.toString().isNotEmpty()
        if (!isValid) {
            Toast.makeText(requireContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
            binding.layoutPassword.setBackgroundResource(R.drawable.background_wrong_input)
        } else {
            binding.layoutPassword.setBackgroundResource(R.drawable.background_input)
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
