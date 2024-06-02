package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.PropertyOpinionAdapter
import com.example.flats4us21.data.PropertyOpinion
import com.example.flats4us21.databinding.FragmentPropertyOpinionsBinding
import com.example.flats4us21.viewmodels.OfferViewModel

class PropertyOpinionsFragment : Fragment() {
    private var _binding: FragmentPropertyOpinionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: PropertyOpinionAdapter
    private lateinit var viewModel: OfferViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        _binding = FragmentPropertyOpinionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offerId = arguments?.getInt(OFFER_ID, -1)
            viewModel.getOffer(offerId!!)

        viewModel.offer.observe(viewLifecycleOwner) { userProfile ->
            if(userProfile != null)
                bindData(userProfile.property.rentOpinions!!)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.opinionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun bindData(rentOpinions: List<PropertyOpinion>) {
        recyclerview = binding.opinionsRecyclerView
        if (!rentOpinions.isNullOrEmpty()) {
            recyclerview.visibility = View.VISIBLE
            binding.noReviewsText.visibility = View.GONE
            adapter = PropertyOpinionAdapter(rentOpinions)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.noReviewsText.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}