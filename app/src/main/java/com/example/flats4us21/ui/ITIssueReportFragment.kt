package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentItIssueReportBinding
import com.example.flats4us21.viewmodels.ITIssueReportViewModel

class ITIssueReportFragment : Fragment() {
    private var _binding: FragmentItIssueReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueViewModel: ITIssueReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        issueViewModel = ViewModelProvider(requireActivity())[ITIssueReportViewModel::class.java]
        _binding = FragmentItIssueReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener{
            val isDataValid = validateData()

            if(isDataValid) {
                issueViewModel.createIssue()
                val fragment = SearchFragment()
                (activity as? DrawerActivity)!!.replaceFragment(fragment)
            }
        }

        binding.cancelButton.setOnClickListener {
            val isDataValid = validateData()

            if(isDataValid) {
                issueViewModel.createIssue()
                val fragment = ITIssueReportFragment()
                (activity as? DrawerActivity)!!.replaceFragment(fragment)
            }
        }
    }

    private fun validateData(): Boolean {
        var test = true

        test = validateIssue() && test
        test = validateDescription() && test

        return test
    }

    private fun validateIssue(): Boolean {
        val id: Int = binding.radioGroup.checkedRadioButtonId
        return if (id!=-1){
            val radio: RadioButton = requireView().findViewById(id)
            binding.warning.isVisible = false
            issueViewModel.issue = radio.text.toString()
            true
        }else{
            binding.warning.isVisible = true
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
            issueViewModel.description = description
            binding.layoutDescription.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }


}