package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us.adapters.PropertyOpinionAdapter
import com.example.flats4us.data.PropertyOpinion
import com.example.flats4us.data.utils.Constants.Companion.PROPERTY_ID
import com.example.flats4us.databinding.FragmentPropertyOpinionsBinding
import com.example.flats4us.viewmodels.RealEstateViewModel

class PropertyOpinionsFragment : Fragment() {
    private var _binding: FragmentPropertyOpinionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyOpinionsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val propertyId = arguments?.getInt(PROPERTY_ID, -1) ?: -1
        if (propertyId != -1) {
            viewModel.getProperty(propertyId)
        }
    }

    private fun setupObservers() {
        viewModel.property.observe(viewLifecycleOwner) { property ->
            if(property != null && property.rentOpinions != null) {
                bindData(property.rentOpinions!!)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.opinionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
    }

    private fun bindData(rentOpinions: List<PropertyOpinion>) {
        if (rentOpinions.isNotEmpty()) {
            binding.noReviewsText.visibility = View.GONE
            binding.opinionsRecyclerView.visibility = View.VISIBLE
            binding.opinionsRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.opinionsRecyclerView.adapter = PropertyOpinionAdapter(rentOpinions)
        } else {
            binding.noReviewsText.visibility = View.VISIBLE
            binding.opinionsRecyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
