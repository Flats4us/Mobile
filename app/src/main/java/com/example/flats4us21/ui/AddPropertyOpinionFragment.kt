package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.data.dto.NewRentOpinionDto
import com.example.flats4us21.databinding.FragmentAddPropertyOpinionBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

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
                        (activity as? DrawerActivity)?.goBack()
                    }
                }
            }
        }
    }

    private fun validateData(): Boolean {
        val rating = binding.starRating.rating
        return if (rating < 1) {
            Toast.makeText(context, "Rating must be at least 1", Toast.LENGTH_SHORT).show()
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
