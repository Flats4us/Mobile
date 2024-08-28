package com.example.flats4us.ui

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
import com.example.flats4us.DrawerActivity
import com.example.flats4us.adapters.OwnerPropertyAdapter
import com.example.flats4us.data.Offer
import com.example.flats4us.data.utils.Constants.Companion.OFFER_ID
import com.example.flats4us.databinding.FragmentOwnerOffersBinding
import com.example.flats4us.viewmodels.OfferViewModel

private const val TAG = "OwnerOffersFragment"
class OwnerOffersFragment : Fragment() {
    private var _binding : FragmentOwnerOffersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: OwnerPropertyAdapter
    private lateinit var viewModel: OfferViewModel
    private var fetchedOffers: MutableList<Offer> = mutableListOf()

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
            Log.i(TAG, "Number of offers: ${offers.size}")
            fetchedOffers = offers
            adapter.setOfferList(fetchedOffers)
            adapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerview.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        adapter = OwnerPropertyAdapter(true, fetchedOffers){selectedOffer ->
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