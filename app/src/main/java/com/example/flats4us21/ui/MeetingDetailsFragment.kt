package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.R
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.databinding.FragmentMeetingDetailsBinding
import com.example.flats4us21.viewmodels.MeetingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale

class MeetingDetailsFragment : Fragment() {
    private var _binding : FragmentMeetingDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var meeting : Meeting
    private lateinit var meetingViewModel: MeetingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        @Suppress("DEPRECATION")
        meeting = (arguments?.getParcelable("MEETING") as? Meeting)!!
        _binding = FragmentMeetingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]

        bindMeetingData(meeting)

        meetingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        val bottomNav = binding.bottomNavigationView
        if((DataStoreManager.userRole.value!!.uppercase(Locale.getDefault()) == "STUDENT") && meeting.studentAcceptDate == null){
            bottomNav.menu.findItem(R.id.edit).isVisible = false
            bottomNav.menu.findItem(R.id.cancel).isVisible = false
        } else if((DataStoreManager.userRole.value!!.uppercase(Locale.getDefault()) == "OWNER") && meeting.ownerAcceptDate == null){
            bottomNav.menu.findItem(R.id.edit).isVisible = false
            bottomNav.menu.findItem(R.id.cancel).isVisible = false
        }
        else{
            binding.bottomNavigationView.visibility = View.GONE
        }
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.accept -> sendDecision(true)
                R.id.reject -> sendDecision(false)
                else -> {}
            }
            true
        }
    }

    private fun bindMeetingData(meeting: Meeting) {
        val formatter = ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val meetingDateTime = LocalDateTime.parse(meeting.date, formatter)
        val meetingDate = meetingDateTime.toLocalDate()
        val meetingTime = meetingDateTime.toLocalTime()

        binding.title.text = meetingDate.format(ofPattern("yyyy-MM-dd")).toString()
        binding.time.text = meetingTime.format(ofPattern("HH:mm")).toString()
        binding.reason.text = meeting.reason
        binding.place.text = meeting.place
    }

    private fun sendDecision(decision: Boolean) {
        meetingViewModel.acceptMeeting(meeting.meetingId, decision){
            if (it) {
                binding.bottomNavigationView.visibility = View.GONE
                Toast.makeText(requireContext(), getString(R.string.sent_decision), Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_MEETING = "MEETING"

        fun newInstance(meeting: Meeting): MeetingDetailsFragment {
            val fragment = MeetingDetailsFragment()
            val args = Bundle().apply {
                putParcelable(ARG_MEETING, meeting)
            }
            fragment.arguments = args
            return fragment
        }
    }
}