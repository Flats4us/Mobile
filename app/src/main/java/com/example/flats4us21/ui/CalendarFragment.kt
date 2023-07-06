package com.example.flats4us21.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.CalendarAdapter
import com.example.flats4us21.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedDate : LocalDate = LocalDate.now()

        val calendarRecyclerView : RecyclerView = binding.calendarRecyclerView
        val monthYearText : TextView = binding.monthYearTV
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        val daysInMonth : List<String> = daysInMonthArray(selectedDate)
        monthYearText.text = selectedDate.format(formatter)
        Log.e("CalendarFragment", "Failed to setAdapter: ${daysInMonth.toString()}")
        calendarAdapter = CalendarAdapter(daysInMonth)
        calendarRecyclerView.adapter = calendarAdapter
        calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(selectedDate: LocalDate): List<String> {
        val daysInMonthArray = mutableListOf<String>()
        val yearMonth: YearMonth = YearMonth.from(selectedDate)
        val daysInMonth: Int = yearMonth.lengthOfMonth()

        val firstDayOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek: Int = firstDayOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
