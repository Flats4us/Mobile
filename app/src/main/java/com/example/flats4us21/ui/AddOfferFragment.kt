package com.example.flats4us21.ui

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
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

        val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val fileName = getFileNameFromUri(uri)
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val content = inputStream?.bufferedReader().use { it?.readText() }
                inputStream?.close()
                binding.fileName.text = fileName.toString()
                binding.fileNameLayout.isVisible = true
                binding.addRulesButton.isVisible = false
            }
        }
        binding.addRulesButton.setOnClickListener{
            getContent.launch(arrayOf("application/pdf", "text/plain"))
        }

        binding.deleteButton.setOnClickListener {
            binding.fileNameLayout.isVisible = false
            binding.addRulesButton.isVisible = true
        }
    }

    private fun getFileNameFromUri(uri: Uri): Any {
        var fileName = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    //TODO: create a method to collect all data

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}