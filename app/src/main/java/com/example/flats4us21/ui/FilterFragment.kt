package com.example.flats4us21.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.databinding.FragmentFilterBinding
import com.example.flats4us21.data.FilterCriteria

interface FilterFragmentListener {
    fun onFilterApplied(filterCriteria: FilterCriteria)
}

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    var listener: FilterFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FilterFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterButton.setOnClickListener {
            applyFilter()
        }
    }

    private fun applyFilter() {
        val location = binding.location.text.toString()
        val minSize = binding.minSize.text.toString().toDoubleOrNull()
        val maxSize = binding.maxSize.text.toString().toDoubleOrNull()
        val rooms = binding.rooms.text.toString().toIntOrNull()
        val floor = binding.floor.text.toString().toIntOrNull()
        val minFloor = binding.minFloor.text.toString().toIntOrNull()
        val maxFloor = binding.maxFloor.text.toString().toIntOrNull()
        val minResidents = binding.minResidents.text.toString().toIntOrNull()
        val maxResidents = binding.maxResidents.text.toString().toIntOrNull()

        val filterCriteria = FilterCriteria(
            location = if (location.isNotBlank()) location else null,
            minSize = minSize,
            maxSize = maxSize,
            rooms = rooms,
            floor = floor,
            minFloor = minFloor,
            maxFloor = maxFloor,
            minResidents = minResidents,
            maxResidents = maxResidents
        )

        listener?.onFilterApplied(filterCriteria)
        parentFragmentManager.popBackStack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
