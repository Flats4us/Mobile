package com.example.flats4us.ui

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
import com.example.flats4us.adapters.RentAdapter
import com.example.flats4us.data.Rent
import com.example.flats4us.data.utils.Constants.Companion.RENT_ID
import com.example.flats4us.databinding.FragmentMyRentsBinding
import com.example.flats4us.viewmodels.RentViewModel
import com.google.android.material.tabs.TabLayout

private const val TAG = "MyRentsFragment"

class MyRentsFragment : Fragment() {
    private var _binding: FragmentMyRentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: RentAdapter
    private lateinit var viewModel: RentViewModel
    private var fetchedRents: MutableList<Rent> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[RentViewModel::class.java]
        _binding = FragmentMyRentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRents()
        recyclerview = binding.rentRecyclerView

        viewModel.rents.observe(viewLifecycleOwner) { rents ->
            Log.i(TAG, "Number of offers: ${rents.size}")
            fetchedRents = rents
            updateRentList(false)
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

        adapter = RentAdapter(fetchedRents) { selectedRent ->
            val bundle = Bundle()
            bundle.putInt(RENT_ID, selectedRent.rentId)
            val fragment = RentDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())


        binding.buttonsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> updateRentList(false)
                    1 -> updateRentList(true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

    }

    private fun updateRentList(showFinished: Boolean) {
        val filteredRents = fetchedRents.filter { it.isFinished == showFinished }
        adapter.setRentList(filteredRents)
        adapter.notifyDataSetChanged()

        if (filteredRents.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.rentRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.rentRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
