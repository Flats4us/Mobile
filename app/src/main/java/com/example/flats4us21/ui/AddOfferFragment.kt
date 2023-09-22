package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.adapters.PropertySpinnerAdapter
import com.example.flats4us21.databinding.FragmentAddOfferBinding
import com.example.flats4us21.viewmodels.PropertyViewModel

class AddOfferFragment : Fragment() {
    private var _binding: FragmentAddOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        propertyViewModel = ViewModelProvider(requireActivity())[PropertyViewModel::class.java]
        _binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val properties = propertyViewModel.getUserProperties()
        val adapter = PropertySpinnerAdapter(requireContext(), properties)

        binding.spinner.adapter = adapter

        //TODO: set the button to add a file
    }

    //TODO: create method to collect all data

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}