package com.example.yourapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.flats4us21.R

class ReportIssueFragment : Fragment() {

    private lateinit var checkBoxAppIssue: CheckBox
    private lateinit var checkBoxPaymentIssue: CheckBox
    private lateinit var checkBoxProfileIssue: CheckBox
    private lateinit var editTextOtherIssue: EditText
    private lateinit var editTextIssueDescription: EditText
    private lateinit var buttonSubmitIssue: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_issue, container, false)

        checkBoxAppIssue = view.findViewById(R.id.checkBoxAppIssue)
        checkBoxPaymentIssue = view.findViewById(R.id.checkBoxPaymentIssue)
        checkBoxProfileIssue = view.findViewById(R.id.checkBoxProfileIssue)
        editTextOtherIssue = view.findViewById(R.id.editTextOtherIssue)
        editTextIssueDescription = view.findViewById(R.id.editTextIssueDescription)
        buttonSubmitIssue = view.findViewById(R.id.buttonSubmitIssue)

        buttonSubmitIssue.setOnClickListener {
            submitIssue()
        }

        return view
    }

    private fun submitIssue() {
        // Here, implement what happens when the user submits an issue.
        // For example, you can gather the information and send it to a server or save it locally.

        val issueDescription = editTextIssueDescription.text.toString()
        val isAppIssue = checkBoxAppIssue.isChecked
        val isPaymentIssue = checkBoxPaymentIssue.isChecked
        val isProfileIssue = checkBoxProfileIssue.isChecked
        val otherIssue = editTextOtherIssue.text.toString()

        // TODO: Implement your submission logic here
    }
}
