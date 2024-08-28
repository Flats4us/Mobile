package com.example.flats4us.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Offer
import com.example.flats4us.data.Rent
import com.example.flats4us.data.dto.NewMeetingDto
import com.example.flats4us.data.utils.Constants.Companion.OFFER_ID
import com.example.flats4us.data.utils.Constants.Companion.RENT_ID
import com.example.flats4us.databinding.FragmentAddMeetingBinding
import com.example.flats4us.viewmodels.MeetingViewModel
import com.example.flats4us.viewmodels.OfferViewModel
import com.example.flats4us.viewmodels.RentViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

private const val TAG = "AddMeetingFragment"
class AddMeetingFragment : Fragment() {
    private var _binding: FragmentAddMeetingBinding? = null
    private val binding get() = _binding!!
    private lateinit var meetingViewModel: MeetingViewModel
    private lateinit var offerViewModel: OfferViewModel
    private lateinit var rentViewModel: RentViewModel
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
        val rentId = arguments?.getInt(RENT_ID)

        meetingViewModel = ViewModelProvider(requireActivity())[MeetingViewModel::class.java]
        offerViewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        rentViewModel = ViewModelProvider(requireActivity())[RentViewModel::class.java]

        if(rentId != null && rentId != 0) {
            rentViewModel.getRent(rentId)
        } else {
            offerViewModel.getOffer(offerId!!)
        }

        rentViewModel.rent.observe(viewLifecycleOwner) { rent ->
            if (rent != null) {
                bindRentData(rent)
            }
        }

        offerViewModel.offer.observe(viewLifecycleOwner) { offer ->
            if(offer != null) {
                bindOfferData(offer)
            }
        }

        meetingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                meetingViewModel.clearErrorMessage()
            }
        }

        binding.layoutDate.setOnClickListener{
            clickDatePicker(binding.textDate)
        }

        binding.createMeetingButton.setOnClickListener {
            collectData(offerId)
            meetingViewModel.createMeeting {
                if(it){
                    Toast.makeText(requireContext(), getString(R.string.added_meeting), Toast.LENGTH_LONG).show()
                    (activity as? DrawerActivity)!!.goBack()
                }
            }
        }

    }

    private fun bindOfferData(offer: Offer) {
        binding.layoutOwner.visibility = View.VISIBLE
        binding.layoutMainTenant.visibility = View.GONE
        val ownerUrl = "$URL/${offer.owner.profilePicture?.path}"
        Log.i(TAG, ownerUrl)
        binding.ownerPhoto.load(ownerUrl) {
            error(R.drawable.baseline_person_24)
        }
        binding.userInfo.text = getString(R.string.name_and_surname, offer.owner.name, offer.owner.surname)
    }

    private fun bindRentData(rent: Rent) {
        val today: LocalDate = LocalDate.now()
        binding.textDate.text = today.toString()
        binding.layoutOwner.visibility = View.VISIBLE
        binding.layoutMainTenant.visibility = View.VISIBLE
        val ownerUrl = "$URL/${rent.owner.profilePicture?.path}"
        Log.i(TAG, ownerUrl)
        binding.ownerPhoto.load(ownerUrl) {
            error(R.drawable.baseline_person_24)
        }
        binding.userInfo.text = getString(R.string.name_and_surname, rent.owner.name, rent.owner.surname)
        val mainTenant = rent.tenants.firstOrNull{ it.userId == rent.mainTenantId }
        val mainTenantUrl = "$URL/${mainTenant?.profilePicture?.path ?: ""}"
        Log.i(TAG, mainTenantUrl)
        binding.mainTenantPhoto.load(mainTenantUrl) {
            error(R.drawable.baseline_person_24)
        }
        binding.tenantInfo.text = mainTenant?.fullName ?: ""


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

        meetingViewModel.meeting = NewMeetingDto(
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