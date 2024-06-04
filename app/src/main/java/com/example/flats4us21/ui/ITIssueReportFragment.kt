package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.data.NewTechnicalProblemsDto
import com.example.flats4us21.databinding.FragmentItIssueReportBinding
import com.example.flats4us21.viewmodels.TechnicalProblemsViewModel

class ITIssueReportFragment : Fragment() {
    private var _binding: FragmentItIssueReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueViewModel: TechnicalProblemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        issueViewModel = ViewModelProvider(requireActivity())[TechnicalProblemsViewModel::class.java]
        _binding = FragmentItIssueReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        issueViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        issueViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
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
        val id: Int = binding.radioGroup.checkedRadioButtonId
        val description = binding.description.text.toString()

        return NewTechnicalProblemsDto(id, description)
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