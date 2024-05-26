package com.example.flats4us21.ui

import com.example.flats4us21.adapters.QuestionAdapter
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.PropertySpinnerAdapter
import com.example.flats4us21.data.Property
import com.example.flats4us21.databinding.FragmentAddOfferBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import com.example.flats4us21.viewmodels.UserViewModel
import java.time.LocalDate
import java.util.*

private const val TAG = "AddOfferFragment"
class AddOfferFragment : Fragment() {
    private var _binding: FragmentAddOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var offerViewModel: OfferViewModel
    private lateinit var userViewModel: UserViewModel
    private var selectedProperty: Property? = null
    private var fileContent: String? = null
    private val fetchedProperties: MutableList<Property> = mutableListOf()
    private lateinit var adapter: PropertySpinnerAdapter
    private var selectedStartDate : LocalDate? = null
    private var selectedEndDate : LocalDate? = null
    private lateinit var surveyAdapter: QuestionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offerViewModel = ViewModelProvider(this)[OfferViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        offerViewModel.getUserProperties(true)
        userViewModel.getQuestionList(DataStoreManager.userRole.value.toString())

        val filter: InputFilter = CurrencyInputFilter()
        binding.price.filters = arrayOf(filter)

        offerViewModel.userProperties.observe(viewLifecycleOwner) { userProperties ->
            Log.i(TAG, "Number of offers: ${userProperties.size}")
            fetchedProperties.addAll(userProperties)
            adapter.notifyDataSetChanged()
        }
        offerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        offerViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        adapter = PropertySpinnerAdapter(requireContext(), fetchedProperties)

        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedProperty = p0?.getItemAtPosition(p2) as Property
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedProperty = null
            }
        }

        userViewModel.questionList.observe(viewLifecycleOwner) { questions ->
            Log.i(TAG, "Number of questions: ${questions.size}")
            surveyAdapter = QuestionAdapter(questions)
            val recyclerView: RecyclerView = binding.questionRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = surveyAdapter
            surveyAdapter.notifyDataSetChanged()
            Log.i(TAG, "Number of questions: ${surveyAdapter.itemCount}")
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

        binding.layoutStartDate.setOnClickListener {
            selectedStartDate = clickDatePicker(binding.startDate)
        }

        binding.layoutEndDate.setOnClickListener {
            selectedEndDate = clickDatePicker(binding.endDate)
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
            Log.d(TAG, "Size: ${adapter.count}")
            collectData()
        }
    }

    private fun clickDatePicker(textView: TextView) : LocalDate? {
        var selectedDate : LocalDate? = LocalDate.now()
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            selectedDate = LocalDate.of(selectedYear, selectedMonth+1, selectedDayOfMonth)
            textView.text = selectedDate.toString()
        },
            year,
            month,
            day)
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        return selectedDate
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
        if (validateData()) {
            val userResponses = surveyAdapter.getUserResponses()

            if (userResponses["smokingAllowed"] == null ||
                userResponses["partiesAllowed"] == null ||
                userResponses["animalsAllowed"] == null ||
                userResponses["gender"] == null
            ) {
                Toast.makeText(requireContext(), "Please answer all required questions.", Toast.LENGTH_LONG).show()
                return
            }

            offerViewModel.smokingAllowed = userResponses["smokingAllowed"] as? Boolean ?: false
            offerViewModel.partiesAllowed = userResponses["partiesAllowed"] as? Boolean ?: false
            offerViewModel.animalsAllowed = userResponses["animalsAllowed"] as? Boolean ?: false
            offerViewModel.gender = userResponses["gender"] as? Int ?: 0

            offerViewModel.startDate = selectedStartDate!!.atStartOfDay()
            offerViewModel.endDate = selectedEndDate!!.atStartOfDay()
            offerViewModel.createOffer{ result ->
                if(result){
                    val fragment = SearchFragment()
                    (activity as? DrawerActivity)!!.replaceFragment(fragment)
                }
            }
        }
    }

    private fun validateData(): Boolean {
        var test = true

        test = validatePrice() && test
        test = validateDeposit() && test
        test = validateTextView(binding.startDate, binding.layoutStartDate, binding.layoutStartDateHeader, binding.layoutStartDateWithHeader) && test
        test = validateTextView(binding.endDate, binding.layoutEndDate, binding.layoutEndDateHeader, binding.layoutEndDateWithHeader) && test
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
            offerViewModel.price = priceText.toInt()
            binding.layoutPrice.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_input)
            true
        }
    }

    private fun validateDeposit() : Boolean {
        val depositText = binding.deposit.text.toString()
        return if (depositText.isEmpty()) {
            binding.layoutDeposit.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_wrong_input)
            false
        } else {
            offerViewModel.deposit = depositText.toInt()
            binding.layoutDeposit.background =
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

    private fun validateTextView(textView: TextView, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = textView.text.toString()
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
