package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.databinding.FragmentMeetingDetailsBinding
import java.time.format.DateTimeFormatter.ofPattern

class MeetingDetailsFragment : Fragment() {
    private var _binding : FragmentMeetingDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var meeting : Meeting
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        meeting = (arguments?.get(ARG_MEETING) as? Meeting)!!
        _binding = FragmentMeetingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindMeetingData(meeting)
    }

    private fun bindMeetingData(meeting : Meeting){
        binding.title.text = meeting.date.toLocalDate().toString()
        binding.time.text = meeting.date.toLocalTime().format(ofPattern("HH:mm")).toString()
        binding.reason.text = meeting.reason
        binding.place.text = "${meeting.offer.property.city} ${meeting.offer.property.street}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_MEETING = "meeting"

        fun newInstance(meeting: Meeting): MeetingDetailsFragment {
            val fragment = MeetingDetailsFragment()
            val args = Bundle().apply {
                putSerializable(ARG_MEETING, meeting)
            }
            fragment.arguments = args
            return fragment
        }
    }
}