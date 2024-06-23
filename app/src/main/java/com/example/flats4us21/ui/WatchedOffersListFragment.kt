package com.example.flats4us21.ui

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
import com.example.flats4us21.adapters.OwnerPropertyAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.utils.Constants
import com.example.flats4us21.databinding.FragmentWatchedOffersListBinding
import com.example.flats4us21.viewmodels.OfferViewModel
import com.example.flats4us21.viewmodels.WatchedOffersViewModel

private const val TAG = "SearchFragment"
class WatchedOffersListFragment : Fragment() {
    private var _binding: FragmentWatchedOffersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: OwnerPropertyAdapter
    private lateinit var viewModel: WatchedOffersViewModel
    private lateinit var offersViewModel: OfferViewModel
    private var fetchedOffers: MutableList<Offer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[WatchedOffersViewModel::class.java]
        offersViewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        _binding = FragmentWatchedOffersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview = binding.propertyRecyclerView
        viewModel.pageNumber = 1
        viewModel.getWatchedOffers()

        viewModel.watchedOffers.observe(viewLifecycleOwner) { offers ->
            if(offers.isNotEmpty()) {
                binding.noReviewsText.visibility = View.GONE
                Log.i(TAG, "Number of offers: ${offers.size}")
                fetchedOffers = offers as MutableList<Offer>
                adapter.setOfferList(fetchedOffers)
                adapter.notifyDataSetChanged()
                val totalPages = viewModel.offersNumber / Constants.QUERY_PAGE_SIZE + 2
                isLastPage = viewModel.pageNumber == totalPages
            } else {
                binding.noReviewsText.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
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

        setRecyclerView()
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

            val isNotLoadingAndNotLastPage = !viewModel.isLoading.value!! && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible  = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getWatchedOffers()
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
        adapter = OwnerPropertyAdapter(false, fetchedOffers){selectedOffer ->
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, selectedOffer.offerId)
            val fragment = OfferDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        adapter.setViewModel(offersViewModel)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.addOnScrollListener(this@WatchedOffersListFragment.scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}