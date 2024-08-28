package com.example.flats4us.ui

import android.os.Bundle
import android.text.InputType
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
import com.example.flats4us.data.dto.NewPasswordDto
import com.example.flats4us.databinding.FragmentSettingsChangePasswordBinding
import com.example.flats4us.viewmodels.UserViewModel


class SettingsChangePasswordFragment : Fragment() {
    private var _binding : FragmentSettingsChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        _binding = FragmentSettingsChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

        binding.oldPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.oldPasswordToggle, binding.oldPassword)
        }

        binding.newPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.newPasswordToggle, binding.newPassword)
        }

        binding.confirmPasswordToggle.setOnClickListener{
            setPasswordVisibility(binding.confirmPasswordToggle, binding.confirmNewPassword)
        }
        binding.editPasswordButton.setOnClickListener {
            if(validateDataForPassword()){
                collectDataForPassword()
                userViewModel.changePassword {
                    if(it) {
                        Toast.makeText(requireContext(), getString(R.string.password_changed), Toast.LENGTH_LONG).show()
                        (activity as? DrawerActivity)!!.goBack()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.invalid_data), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun collectDataForPassword() {
        val newPasswordDto = NewPasswordDto(
            binding.oldPassword.text.toString(),
            binding.newPassword.text.toString()
        )
        userViewModel.newPassword = newPasswordDto
    }

    private fun setPasswordVisibility(toggle : ImageButton, password : EditText) {
        if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun validateDataForPassword() : Boolean {
        val isOldPasswordValid = binding.oldPassword.text.isNotEmpty()
        val isNewPasswordValid = binding.newPassword.text.isNotEmpty()
        val isConfirmPasswordValid = binding.confirmNewPassword.text.isNotEmpty()
        val arePasswordsTheSame = arePasswordsTheSame(binding.newPassword, binding.confirmNewPassword)

        return isOldPasswordValid && isNewPasswordValid && isConfirmPasswordValid && arePasswordsTheSame
    }

    private fun arePasswordsTheSame(password: EditText, repeatPassword: EditText): Boolean {
        val arePasswordsValid = password.text.toString() == repeatPassword.text.toString()
        if(!arePasswordsValid){
            Toast.makeText(requireContext(), getString(R.string.passwords_not_matching), Toast.LENGTH_LONG).show()
        }
        return arePasswordsValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}