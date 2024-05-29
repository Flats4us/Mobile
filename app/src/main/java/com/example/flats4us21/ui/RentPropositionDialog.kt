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
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.databinding.FragmentRentPropositionDialogBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel

const val USER_ID = "USER_ID"
private const val TAG = "RentPropositionDialogFragment"
class RentPropositionDialogFragment : Fragment()  {
    private var _binding: FragmentRentPropositionDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : DetailOfferViewModel
    private lateinit var fetchedRent : RentProposition

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentPropositionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DetailOfferViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            if (resultMessage != null) {
                Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
            }
        }

        val rentPropositionId = arguments?.getInt(RENT_PROPOSITION_ID, -1)
        val offerId = arguments?.getInt(OFFER_ID, -1)
        Log.d(TAG, "rentPropositionId: $rentPropositionId")
        viewModel.getRentProposition(rentPropositionId!!)

        viewModel.rent.observe(viewLifecycleOwner) {
            fetchedRent = it
            Log.d(TAG, "fetchedRent: $fetchedRent")
            bindData(it)
        }

        binding.no.setOnClickListener {
            viewModel.addRentDecision(offerId!!, false) {
                if(it){
                    (activity as? DrawerActivity)!!.goBack()
                }
            }
        }

        binding.yes.setOnClickListener {
            viewModel.addRentDecision(rentPropositionId, true){
                if(it) {
                    (activity as? DrawerActivity)!!.goBack()
                }
            }
        }
    }

    private fun bindData(rent: RentProposition) {
        val adapter = ProfileAdapter(
            rent.tenants,
            { position ->
                val userId = rent.tenants[position].userId
                Log.i(TAG, "userId: $userId")
                val bundle = Bundle().apply {
                    putInt(USER_ID, userId)
                }
                val fragment = ProfileFragment().apply {
                    arguments = bundle
                }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            },
            null,
            false
        )

        binding.startDate.text = rent.startDate.split("T")[0]
        binding.duration.text = rent.duration.toString()
        binding.usersRecyclerView.adapter = adapter
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())


    }

}