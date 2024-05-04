package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.databinding.FragmentWatchedOffersListBinding
import com.example.flats4us21.viewmodels.OfferViewModel

class WatchedOffersListFragment : Fragment() {
    private var _binding: FragmentWatchedOffersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var offerViewModel: OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        offerViewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        _binding = FragmentWatchedOffersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = binding.propertyRecyclerView
        val offers = offerViewModel.getWatchedOffers()
        val adapter = PropertyAdapter(false, offers){selectedOffer ->
            offerViewModel.selectedOffer =  selectedOffer
            val fragment = OfferDetailFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        adapter.setViewModel(offerViewModel)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

}