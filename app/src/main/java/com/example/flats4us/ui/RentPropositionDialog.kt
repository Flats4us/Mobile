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
import com.example.flats4us.DrawerActivity
import com.example.flats4us.adapters.ProfileAdapter
import com.example.flats4us.data.RentProposition
import com.example.flats4us.data.utils.Constants.Companion.OFFER_ID
import com.example.flats4us.data.utils.Constants.Companion.RENT_PROPOSITION_ID
import com.example.flats4us.data.utils.Constants.Companion.USER_ID
import com.example.flats4us.databinding.FragmentRentPropositionDialogBinding
import com.example.flats4us.viewmodels.DetailOfferViewModel


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

        viewModel.resultMessage.observe(viewLifecycleOwner) { resultMessage ->
            resultMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(resultMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    resultMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
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
            viewModel.addRentDecision(offerId!!, true){
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