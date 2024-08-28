package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.data.dto.NewRentOpinionDto
import com.example.flats4us.data.utils.Constants.Companion.RENT_ID
import com.example.flats4us.databinding.FragmentAddPropertyOpinionBinding
import com.example.flats4us.viewmodels.RealEstateViewModel

class AddPropertyOpinionFragment : Fragment() {
    private var _binding: FragmentAddPropertyOpinionBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPropertyOpinionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

        val rentId = arguments?.getInt(RENT_ID) ?: return

        setupButtons(rentId)

        realEstateViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(
                    errorMessage,
                    "string",
                    requireContext().packageName
                )
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                realEstateViewModel.clearErrorMessage()
            }
        }
    }

    private fun setupButtons(rentId: Int) {
        binding.cancelButton.setOnClickListener {
            (activity as? DrawerActivity)?.goBack()
        }

        binding.addButton.setOnClickListener {
            if (validateData()) {
                collectData()
                realEstateViewModel.addRentOpinion(rentId) {
                    if (it) {
                        Toast.makeText(requireContext(), getString(R.string.added_opinion), Toast.LENGTH_LONG).show()
                        (activity as? DrawerActivity)?.goBack()
                    }
                }
            }
        }
    }

    private fun validateData(): Boolean {
        val rating = binding.starRating.rating
        return if (rating < 1) {
            Toast.makeText(context, getString(R.string.min_rating), Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }

    private fun collectData() {
        val rating = binding.starRating.rating * 2
        val service = binding.service.value.toInt()
        val location = binding.location.value.toInt()
        val equipment = binding.equipment.value.toInt()
        val qualityForMoney = binding.qualityForMoney.value.toInt()
        val description = binding.reviewEditText.text.toString()

        realEstateViewModel.newRentOpinionDto = NewRentOpinionDto(
            rating.toInt(),
            service,
            location,
            equipment,
            qualityForMoney,
            description
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
