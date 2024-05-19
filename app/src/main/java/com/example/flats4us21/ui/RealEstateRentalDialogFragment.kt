package com.example.flats4us21.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.R
import com.example.flats4us21.adapters.EmailRoommateAdapter
import com.example.flats4us21.data.dto.NewRentProposition
import com.example.flats4us21.databinding.FragmentRealEstateRentalDialogBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import java.time.LocalDate
import java.util.*

private const val TAG = "RealEstateRentalDialogFragment"
class RealEstateRentalDialogFragment(private val detailOfferViewModel: DetailOfferViewModel) : DialogFragment() {
    private var emails : MutableList<String> = mutableListOf()
    private lateinit var binding: FragmentRealEstateRentalDialogBinding
    private var selectedRentalDate : LocalDate? = null
    private var warnings : MutableList<String> = mutableListOf()
    private var rent : NewRentProposition? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val offerId = arguments?.getInt(OFFER_ID, -1)
            val builder = AlertDialog.Builder(it)
            binding = FragmentRealEstateRentalDialogBinding.inflate(layoutInflater)

            binding.layoutRentalDate.setOnClickListener {
                selectedRentalDate = clickDatePicker(binding.rentalDate)
            }

            val adapter = EmailRoommateAdapter(emails)

            binding.addEmailButton.setOnClickListener {
                isEmailValid(binding.emailEditText, binding.layoutEmail) { isValid ->
                    if (isValid) {
                        emails.add(binding.emailEditText.text.toString())
                        binding.emailEditText.text.clear()
                        adapter.notifyItemInserted(emails.lastIndex)
                    } else {
                        Toast.makeText(requireContext(), "Podano zły email", Toast.LENGTH_LONG).show()
                    }
                }
            }

            binding.linksRecyclerView.adapter = adapter
            binding.linksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            builder
                .setTitle("Wynajmij")
                .setCancelable(false)
                .setView(binding.root)
                .setPositiveButton("Wynajmij",null)
                .setNegativeButton("Anuluj") { dialog, _ ->
                    dialog.dismiss()
                }

            val mAlertDialog : AlertDialog = builder.create()
                mAlertDialog.setOnShowListener {
                val b: Button = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                b.setOnClickListener {
                    warnings.clear()
                    if (validateData()) {
                        rent = NewRentProposition(
                            emails,
                            selectedRentalDate!!.toString(),
                            binding.rentalPeriod.text.toString().toInt()
                        )
                        detailOfferViewModel.setRentValue(rent)
                        if (offerId != null) {
                            detailOfferViewModel.addRentProposition(offerId){result ->
                                if(result){
                                    dismiss()
                                }
                            }
                        }
                    } else {
                        warnings.forEach { warning ->
                            Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                }
            mAlertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isEmailValid(editText: EditText, editTextLayout: ViewGroup, callback: (Boolean) -> Unit) {
        val email = editText.text.toString().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isNotInList = !emails.contains(email)

        detailOfferViewModel.checkEmail(email) { exists ->
            val allConditionsMet = isValid && isNotInList && exists
            Log.i(TAG, "isEmail: $isValid, isNotInList: $isNotInList, exists: $exists, all: $allConditionsMet")
            editTextLayout.setBackgroundResource(if (allConditionsMet) R.drawable.background_input else R.drawable.background_wrong_input)
            callback(allConditionsMet)
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

    private fun validateData(): Boolean {
        val isRentalDateValid = validateOptionalTextView(binding.rentalDate, binding.layoutRentalDate, binding.layoutRentalDateHeader, binding.layoutRentalDateWithHeader)
        val isRentalPeriodValid = validateOptionalEditText(binding.rentalPeriod, binding.layoutRentalPeriod, binding.layoutRentalPeriodHeader, binding.layoutRentalPeriodWithHeader)

        return isRentalDateValid && isRentalPeriodValid
    }

    private fun validateOptionalEditText(editText: EditText, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isNotEmpty = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE
        val isValid = isNotEmpty && isRequired && isVisible

        if (!isNotEmpty){
            warnings.add("Nie podano okresu wynajęcia!")
        }

        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

    private fun validateOptionalTextView(textView: TextView, editTextLayout : ViewGroup, header : TextView, layoutWithHeader : ViewGroup): Boolean {
        val text = textView.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        val isVisible = layoutWithHeader.visibility == View.VISIBLE

        if (!isValid){
            warnings.add("Nie podano daty!")
        }

        editTextLayout.setBackgroundResource(if (isValid || !isRequired || !isVisible) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired || !isVisible
    }
}