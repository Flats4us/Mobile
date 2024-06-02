package com.example.flats4us21.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.databinding.FragmentAddMeetingBinding
import com.example.flats4us21.viewmodels.MeetingViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar


class AddMeetingFragment : Fragment() {
    private var _binding: FragmentAddMeetingBinding? = null
    private val binding get() = _binding!!
    private lateinit var meetingViewModel: MeetingViewModel
    private var selectedMeetingDate : LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offerId = arguments?.getInt(OFFER_ID, -1)

        meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]

        binding.layoutDate.setOnClickListener{
            clickDatePicker(binding.textDate)
        }

        binding.createMeetingButton.setOnClickListener {
            collectData(offerId)
            meetingViewModel.createMeeting {
                if(it){
                    (activity as? DrawerActivity)!!.goBack()
                }
            }
        }

    }

    private fun collectData(offerId: Int?) {
        val localDate = selectedMeetingDate
        val timeHour = binding.timePicker.hour
        val timeMinute = binding.timePicker.minute
        val localTime = LocalTime.of(timeHour, timeMinute)
        val localDateTime = LocalDateTime.of(localDate, localTime)
        Log.d("localDateTime", localDateTime.toString())
        val place = binding.place.text.toString()
        val description = binding.description.text.toString()

        meetingViewModel.meeting = Meeting(
            localDateTime.toString(),
            place,
            description,
            offerId!!
        )
    }

    private fun clickDatePicker(textView: TextView) : LocalDate? {
        var selectedDate : LocalDate? = LocalDate.now()
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            selectedDate = LocalDate.of(selectedYear, selectedMonth+1, selectedDayOfMonth)
            Log.d("SelectedDate", selectedDate.toString())
            selectedMeetingDate = selectedDate
            textView.text = selectedDate.toString()
        },
            year,
            month,
            day)
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
        return selectedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}