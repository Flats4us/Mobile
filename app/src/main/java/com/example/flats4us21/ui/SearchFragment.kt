package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.databinding.FragmentSearchBinding
import com.example.flats4us21.viewmodels.MainViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        recyclerview = binding.propertyRecyclerView
        val offers = viewModel.loadDataFromDb()
        val adapter = PropertyAdapter(offers){selectedOffer ->
            viewModel.setOffer(selectedOffer)
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.frameLayout, OfferDetailFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        val filterButton = binding.filterButton
        filterButton.setOnClickListener {
            // TODO: Filtry
        }
    }
}
