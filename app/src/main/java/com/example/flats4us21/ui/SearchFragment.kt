package com.example.flats4us21.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.flats4us21.databinding.FragmentSearchBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import kotlin.math.ceil

private const val TAG = "SearchFragment"
const val OFFER_ID = "OFFER_ID"
class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: PropertyAdapter
    private lateinit var viewModel: OfferViewModel
    private var fetchedOffers: MutableList<Offer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        viewModel.getOffers()
        recyclerview = binding.propertyRecyclerView

        viewModel.offers.observe(viewLifecycleOwner) { offers ->
            Log.i(TAG, "Number of offers: ${offers.size}")
            fetchedOffers = offers as MutableList<Offer>
            adapter.setOfferList(fetchedOffers)
            adapter.notifyDataSetChanged()
            val totalPages = ceil(viewModel.offersNumber.toDouble() / QUERY_PAGE_SIZE).toInt()
            Log.i(TAG, "Number of total pages: $totalPages")
            isLastPage = viewModel.pageNumber == totalPages


            if (fetchedOffers.isEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.propertyRecyclerView.visibility = View.GONE
            } else {
                binding.noDataText.visibility = View.GONE
                binding.propertyRecyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading && viewModel.pageNumber == 1) View.VISIBLE else View.GONE
            recyclerview.visibility = if (isLoading && viewModel.pageNumber == 1) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            if(resultMessage != null) {
                Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
            }
        }

        setRecyclerView()

        val filterButton = binding.filterButton
        filterButton.setOnClickListener {
            val fragment = FilterFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = viewModel.isLoading.value == false && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible  = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getOffers()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setRecyclerView() {
        adapter = PropertyAdapter(false, fetchedOffers){selectedOffer ->
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, selectedOffer.offerId)
            viewModel.selectedOffer = selectedOffer
            val fragment = OfferDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        adapter.setViewModel(viewModel)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.addOnScrollListener(this@SearchFragment.scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
