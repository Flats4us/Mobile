package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.DrawerActivity
import com.example.flats4us.adapters.MeetingAdapter
import com.example.flats4us.data.Meeting
import com.example.flats4us.data.utils.Constants.Companion.MEETING_ID
import com.example.flats4us.databinding.FragmentMeetingListBinding
import com.example.flats4us.viewmodels.MeetingViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MeetingListFragment : Fragment() {
    private var _binding: FragmentMeetingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedDate : LocalDate
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var meetingViewModel: MeetingViewModel
    private lateinit var meetingAdapter: MeetingAdapter
    private lateinit var dateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingListBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        emptyTextView = binding.emptyTextView
        meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]
        @Suppress("DEPRECATION")
        selectedDate = (arguments?.getSerializable("selectedDate") as? LocalDate)!!
        dateTextView = binding.dateTextView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateTextView.text = selectedDate.toString()
        meetingAdapter = MeetingAdapter {
            val bundle = Bundle()
            bundle.putInt(MEETING_ID, it.meetingId)
            val fragment = MeetingDetailsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = meetingAdapter
        }


        val meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]

        meetingViewModel.meetings.observe(viewLifecycleOwner) { meetings ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            updateMeetingsList(meetings.filter { LocalDateTime.parse(it.date, formatter).toLocalDate() == selectedDate })
        }
    }

    private fun updateMeetingsList(meetings: List<Meeting>) {
        if (meetings.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
            meetingAdapter.updateData(meetings)
        } else {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
