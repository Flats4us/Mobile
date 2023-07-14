package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.adapters.CalendarAdapter
import com.example.flats4us21.databinding.FragmentCalendarBinding
import com.example.flats4us21.viewmodels.MeetingViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(), CalendarAdapter.OnCellClickListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var monthYearText: TextView
    private lateinit var selectedDate: LocalDate
    private lateinit var meetingViewModel: MeetingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
        val nextButton = binding.nextButton
        val prevButton = binding.prevButton
        nextButton.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
        prevButton.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }
    }

    private fun initWidgets() {
        calendarRecyclerView = binding.calendarRecyclerView
        monthYearText = binding.monthYearTV
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val meetingsOfMonth = meetingViewModel.getMeetingsOfMonth(selectedDate.month, selectedDate.year)
        val calendarAdapter = CalendarAdapter(daysInMonth, this, meetingsOfMonth)
        val layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    override fun onCellClick(date: String) {
        if (date.isNotEmpty()) {
            val day = date.toInt()
            val month = selectedDate.monthValue
            val year = selectedDate.year
            val selectedLocalDate = LocalDate.of(year, month, day)
            val meetingListFragment = MeetingListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("selectedDate", selectedLocalDate)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.meetingfragment, meetingListFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
