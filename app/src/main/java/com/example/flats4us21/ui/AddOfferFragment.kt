package com.example.flats4us21.ui

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.PropertySpinnerAdapter
import com.example.flats4us21.data.Property
import com.example.flats4us21.databinding.FragmentAddOfferBinding
import com.example.flats4us21.viewmodels.OfferViewModel

class AddOfferFragment : Fragment() {
    private var _binding: FragmentAddOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var offerViewModel: OfferViewModel
    private var selectedProperty: Property? = null
    private var fileContent: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        offerViewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        _binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filter: InputFilter = CurrencyInputFilter()
        binding.price.filters = arrayOf(filter)

        val properties = offerViewModel.getUserProperties()
        val adapter = PropertySpinnerAdapter(requireContext(), properties)

        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedProperty = p0?.getItemAtPosition(p2) as Property
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedProperty = null
            }
        }

        val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val fileName = getFileNameFromUri(uri)
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                fileContent = inputStream?.bufferedReader().use { it?.readText() }
                binding.fileName.text = fileName.toString()
                binding.fileNameLayout.isVisible = true
                binding.addRulesButton.isVisible = false
                binding.warning.isVisible = false
            }
        }
        binding.addRulesButton.setOnClickListener {
            getContent.launch(arrayOf("application/pdf", "text/plain"))
        }

        binding.deleteButton.setOnClickListener {
            binding.fileNameLayout.isVisible = false
            binding.addRulesButton.isVisible = true
            fileContent = null
        }

        binding.addOfferButton.setOnClickListener {
            collectData()
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

    private fun collectData() {
        val isDataValid = validateData()

        if (isDataValid) {
            offerViewModel.createOffer()
            val fragment = SearchFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    private fun validateData(): Boolean {
        var test = true

        test = validatePrice() && test
        test = validateRentalPeriod() && test
        test = validateSelectedProperty() && test
        test = validateDescription() && test
        test = validateFileContent() && test

        return test
    }

    private fun validatePrice(): Boolean {
        val priceText = binding.price.text.toString()
        return if (priceText.isEmpty()) {
            binding.layoutPrice.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            offerViewModel.price = priceText.toDouble()
            binding.layoutPrice.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }

    private fun validateRentalPeriod(): Boolean {
        val rentalPeriodText = binding.rentalPeriod.text.toString()
        return if (rentalPeriodText.isEmpty()) {
            binding.layoutRentalPeriod.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            offerViewModel.rentalPeriod = rentalPeriodText.toInt()
            binding.layoutRentalPeriod.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }

    private fun validateSelectedProperty(): Boolean {
        return if (selectedProperty == null) {
            binding.layoutProperty.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            offerViewModel.property = selectedProperty
            binding.layoutProperty.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }

    private fun validateDescription(): Boolean {
        val descriptionText = binding.description.text.toString()
        return if (descriptionText.isEmpty()) {
            binding.layoutDescription.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            offerViewModel.description = descriptionText
            binding.layoutDescription.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }

    private fun validateFileContent(): Boolean {
        return if (fileContent == null || fileContent!!.isEmpty()) {
            binding.warning.isVisible = true
            false
        } else {
            binding.warning.isVisible = false
            offerViewModel.rules = fileContent.toString()
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class CurrencyInputFilter(
        val maxDigitsBeforeDecimalPoint: Int = 6,
        val maxDigitsAfterDecimalPoint: Int = 2
    ) : InputFilter {

        override fun filter(
            source: CharSequence, start: Int, end: Int,
            dest: Spanned, dstart: Int, dend: Int
        ): CharSequence? {
            val builder = StringBuilder(dest)
            builder.replace(dstart, dend, source
                .subSequence(start, end).toString())
            return if (!builder.toString().matches(
                    ("(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?").toRegex()
                )
            ) {
                if (source.isEmpty()) dest.subSequence(dstart, dend) else ""
            } else null
        }
    }
}
