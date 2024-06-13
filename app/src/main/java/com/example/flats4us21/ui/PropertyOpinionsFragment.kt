package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.adapters.PropertyOpinionAdapter
import com.example.flats4us21.data.PropertyOpinion
import com.example.flats4us21.databinding.FragmentPropertyOpinionsBinding
import com.example.flats4us21.viewmodels.OfferViewModel

class PropertyOpinionsFragment : Fragment() {
    private var _binding: FragmentPropertyOpinionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyOpinionsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val offerId = arguments?.getInt(OFFER_ID, -1) ?: -1
        if (offerId != -1) {
            viewModel.getOffer(offerId)
        }
    }

    private fun setupObservers() {
        viewModel.offer.observe(viewLifecycleOwner) { offer ->
            offer?.property?.rentOpinions?.let { bindData(it) }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.opinionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun bindData(rentOpinions: List<PropertyOpinion>) {
        if (rentOpinions.isNotEmpty()) {
            binding.noReviewsText.visibility = View.GONE
            binding.opinionsRecyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context)
                adapter = PropertyOpinionAdapter(rentOpinions)
            }
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
