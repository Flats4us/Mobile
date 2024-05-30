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
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.RentAdapter
import com.example.flats4us21.data.Rent
import com.example.flats4us21.databinding.FragmentMyRentsBinding
import com.example.flats4us21.viewmodels.RentViewModel

private const val TAG = "MyRentsFragment"
const val RENT_ID = "RENT_ID"

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
            updateRentList(false) // Default to showing active rents
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerview.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
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

        // Button listeners
        binding.btnActiveRents.setOnClickListener {
            updateRentList(false)
        }

        binding.btnFinishedRents.setOnClickListener {
            updateRentList(true)
        }
    }

    private fun updateRentList(showFinished: Boolean) {
        val filteredRents = fetchedRents.filter { it.isFinished == showFinished }
        adapter.setRentList(filteredRents)
        adapter.notifyDataSetChanged()

        // Show or hide the empty view
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
