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
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentForgotMyPasswordBinding
import com.example.flats4us21.viewmodels.UserViewModel

class ForgotMyPasswordFragment : Fragment() {
    private var _binding : FragmentForgotMyPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var warnings : MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotMyPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        val sendButton = binding.buttonSend
        sendButton.setOnClickListener {
            warnings.clear()
            if(isEmailValid(binding.email, binding.layoutEmail)){
                userViewModel.sendPasswordResetLink(binding.email.text.toString()) {
                    if(it) {
                        (activity as DrawerActivity).goBack()
                    }
                }
            } else {
                warnings.forEach { warning ->
                    Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun isEmailValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if(!isValid){
            warnings.add("Zły email")
        }
        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

}