package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.databinding.FragmentSearchBinding
import com.example.flats4us21.viewmodels.MainViewModel


class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        recyclerview = binding.propertyRecyclerView
        val offers = viewModel.loadDataFromDb()
        val adapter = PropertyAdapter(offers){selectedOffer ->
            viewModel.setOffer(selectedOffer)
            val fragment = OfferDetailFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        val filterButton = binding.filterButton
        filterButton.setOnClickListener {
            val fragment = FilterFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
