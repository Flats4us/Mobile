package com.example.flats4us21.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.FragmentOwnerOffersBinding
import com.example.flats4us21.viewmodels.OfferViewModel

private const val TAG = "OwnerOffersFragment"
class OwnerOffersFragment : Fragment() {
    private var _binding : FragmentOwnerOffersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: PropertyAdapter
    private lateinit var viewModel: OfferViewModel
    private val fetchedOffers: MutableList<Offer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OfferViewModel::class.java]
        viewModel.getMineOffers()
        recyclerview = binding.offerRecyclerView
        binding.offerRecyclerView

        viewModel.offers.observe(viewLifecycleOwner) { offers ->
            Log.i(TAG, "Number of offers: $offers.size")
            fetchedOffers.addAll(offers)
            adapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerview.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        adapter = PropertyAdapter(true, fetchedOffers){selectedOffer ->
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, selectedOffer.offerId)
            viewModel.selectedOffer = selectedOffer
            val fragment = OwnerOfferDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        adapter.setViewModel(viewModel)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener {
            val fragment = AddOfferFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}