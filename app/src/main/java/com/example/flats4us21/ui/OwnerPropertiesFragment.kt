package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.OwnerPropertiesAdapter
import com.example.flats4us21.data.Property
import com.example.flats4us21.databinding.FragmentOwnerPropertiesBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import com.example.flats4us21.viewmodels.RealEstateViewModel

private const val TAG = "NotificationsFragment"
class OwnerPropertiesFragment : Fragment() {
    private var _binding: FragmentOwnerPropertiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var offerViewModel: OfferViewModel
    private lateinit var realEstateViewModel: RealEstateViewModel
    private var fetchedProperties: MutableList<Property> = mutableListOf()
    private lateinit var adapter: OwnerPropertiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerPropertiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offerViewModel = ViewModelProvider(this)[OfferViewModel::class.java]
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]

        offerViewModel.getUserProperties(binding.verified.isChecked)

        offerViewModel.userProperties.observe(viewLifecycleOwner) { userProperties ->
            Log.i(TAG, "Number of offers: ${userProperties.size}")
            fetchedProperties = userProperties as MutableList<Property>
            adapter.setPropertyList(fetchedProperties)
            adapter.notifyDataSetChanged()
        }

        offerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        offerViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        adapter = OwnerPropertiesAdapter(fetchedProperties){selectedProperty ->
            realEstateViewModel.selectedProperty = selectedProperty
            val fragment = OwnerPropertyDetailFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.propertyRecyclerView.adapter = adapter
        binding.propertyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.verified.setOnClickListener {
            offerViewModel.getUserProperties(binding.verified.isChecked)
        }
        binding.fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(IS_CREATING, true)
            val fragment = AddRealEstateFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}