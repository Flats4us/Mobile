package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.data.NewTechnicalProblemsDto
import com.example.flats4us21.databinding.FragmentTechnicalProblemBinding
import com.example.flats4us21.viewmodels.TechnicalProblemsViewModel

private const val TAG = "TechnicalProblemFragment"
class TechnicalProblemFragment : Fragment() {
    private var _binding: FragmentTechnicalProblemBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueViewModel: TechnicalProblemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        issueViewModel = ViewModelProvider(requireActivity())[TechnicalProblemsViewModel::class.java]
        _binding = FragmentTechnicalProblemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        issueViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        issueViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                issueViewModel.clearErrorMessage()
            }
        }


        binding.addButton.setOnClickListener{
            val isDataValid = validateData()

            if(isDataValid) {
                issueViewModel.addTechnicalProblems(collectData()) {
                    if(it) {
                        val fragment = SearchFragment()
                        (activity as? DrawerActivity)!!.replaceFragment(fragment)
                    }
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            (activity as? DrawerActivity)!!.goBack()
        }
    }

    private fun collectData(): NewTechnicalProblemsDto {
        val index = getSelectedRadioButtonIndex(binding.radioGroup)
        val description = binding.description.text.toString()

        val newTechnicalProblemsDto = NewTechnicalProblemsDto(index, description)
        Log.i(TAG, "$newTechnicalProblemsDto")
        return newTechnicalProblemsDto
    }

    private fun getSelectedRadioButtonIndex(radioGroup: RadioGroup): Int {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as? RadioButton
            if (radioButton?.id == checkedRadioButtonId) {
                return i
            }
        }
        return -1
    }

    private fun validateData(): Boolean {
        val isIssueValid = validateIssue()
        val isDescriptionValid = validateDescription()

        return isIssueValid && isDescriptionValid
    }

    private fun validateIssue(): Boolean {
        val id: Int = binding.radioGroup.checkedRadioButtonId
        return if (id!=-1){
            true
        } else {
            Toast.makeText(requireContext(), R.string.no_option_selected, Toast.LENGTH_LONG).show()
            false
        }
    }
    private fun validateDescription(): Boolean {
        val description  = binding.description.text.toString()
        return if(description == ""){
            binding.layoutDescription.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            binding.layoutDescription.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }


}