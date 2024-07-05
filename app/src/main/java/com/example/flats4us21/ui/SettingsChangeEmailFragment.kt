package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentSettingsChangeEmailBinding
import com.example.flats4us21.viewmodels.UserViewModel


class SettingsChangeEmailFragment : Fragment() {
    private var _binding : FragmentSettingsChangeEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentSettingsChangeEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPasswordButton.setOnClickListener {
            if(validateData()){
                userViewModel.changeEmail(binding.email.text.toString()) {
                    if (it) {
                        Toast.makeText(requireContext(), "Email został zmieniony", Toast.LENGTH_LONG).show()
                        userViewModel.getMyProfile()
                    } else {
                        Toast.makeText(requireContext(), "Nie udało się zmienić emailu", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Nie wypełniono poprawnie wszystkich pól", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateData(): Boolean {
        val isEmailValid = isEmailValid(binding.email, binding.layoutEmail)
        val areEmailsTheSame = areEmailsTheSame(binding.email, binding.newEmail)
        return isEmailValid && areEmailsTheSame

    }

    private fun isEmailValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if(!isValid){
            Toast.makeText(requireContext(), "Zły email", Toast.LENGTH_SHORT).show()
        }
        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

    private fun areEmailsTheSame(email: EditText, repeatEmail: EditText): Boolean {
        val arePasswordsValid = email.text.toString() == repeatEmail.text.toString()
        if(!arePasswordsValid){
            Toast.makeText(requireContext(), "Podane emaile różnią się", Toast.LENGTH_LONG).show()
        }
        return arePasswordsValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}