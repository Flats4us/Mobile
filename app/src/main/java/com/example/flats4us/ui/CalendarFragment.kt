package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.R
import com.example.flats4us.adapters.CalendarAdapter
import com.example.flats4us.data.Meeting
import com.example.flats4us.databinding.FragmentCalendarBinding
import com.example.flats4us.viewmodels.MeetingViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(), CalendarAdapter.OnCellClick {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var monthYearText: TextView
    private lateinit var selectedDate: LocalDate
    private lateinit var meetingViewModel: MeetingViewModel
    private var fetchedMeetings: MutableList<Meeting> = mutableListOf()

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
        meetingViewModel.getMeetings()


        meetingViewModel.meetings.observe(viewLifecycleOwner) { meetings ->
            fetchedMeetings = meetings as MutableList<Meeting>
            setMonthView()
        }
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
        val meetingsOfMonth = filterMeetingsForCurrentMonth(fetchedMeetings, selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this, meetingsOfMonth)
        val layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun filterMeetingsForCurrentMonth(meetings: List<Meeting>, now: LocalDate): List<Meeting> {
        val currentYearMonth = YearMonth.from(now)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        return meetings.filter { meeting ->
            val meetingDate = LocalDateTime.parse(meeting.date, formatter).toLocalDate()
            val meetingYearMonth = YearMonth.from(meetingDate)
            meetingYearMonth == currentYearMonth
        }
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        val mondayOfWeek = if (dayOfWeek == 1) 0 else if (dayOfWeek == 0) 6 else dayOfWeek - 1

        for (i in 1..mondayOfWeek) {
            daysInMonthArray.add("")
        }

        for (i in 1..daysInMonth) {
            daysInMonthArray.add(i.toString())
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
                .replace(R.id.meetingFragment, meetingListFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
