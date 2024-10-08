package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.data.dto.NewArgumentDto
import com.example.flats4us.data.utils.Constants.Companion.RENT_ID
import com.example.flats4us.databinding.FragmentAddArgumentBinding
import com.example.flats4us.viewmodels.ArgumentViewModel


class AddArgumentFragment : Fragment() {
    private var _binding : FragmentAddArgumentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : ArgumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddArgumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArgumentViewModel::class.java]
        val rentId = arguments?.getInt(RENT_ID, -1)

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        binding.createArgumentButton.setOnClickListener {
            if (validateData()) {
                val newArgumentDto = NewArgumentDto(binding.title.text.toString() ,binding.description.text.toString(), rentId!!)

                viewModel.addArgument(newArgumentDto) {
                    if (it) {
                        val fragment = ArgumentsFragment()
                        (activity as? DrawerActivity)?.replaceFragment(fragment)
                    }
                }
            }
        }

    }

    private fun validateData(): Boolean {
        val isTitleValid = validateOptionalEditText(binding.title, binding.layoutTitle, binding.layoutTitleHeader, binding.layoutTitleWithHeader)
        val isDescriptionValid = validateOptionalEditText(binding.description, binding.layoutDescription, binding.layoutDescriptionHeader, binding.layoutDescriptionWithHeader)

        return isTitleValid && isDescriptionValid
    }

    private fun validateOptionalEditText(editText: EditText, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        editTextLayout.setBackgroundResource(if (isValid || !isRequired || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired || !isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
