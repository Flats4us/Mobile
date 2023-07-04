package com.example.flats4us21.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.databinding.ActivityFilterBinding
import com.example.flats4us21.databinding.FragmentFilterBinding
import com.example.flats4us21.databinding.FragmentSearchBinding


class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterButton = binding.filterButton
        filterButton.setOnClickListener {
            //TODO: set filter
        }

    }

}