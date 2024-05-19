package com.example.flats4us21.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.databinding.FragmentRentPropositionDialogBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel

const val USER_ID = "USER_ID"
private const val TAG = "RentPropositionDialogFragment"
class RentPropositionDialogFragment() : DialogFragment()  {
    private lateinit var binding: FragmentRentPropositionDialogBinding
    private lateinit var viewModel : DetailOfferViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            viewModel = ViewModelProvider(this)[DetailOfferViewModel::class.java]
            binding = FragmentRentPropositionDialogBinding.inflate(layoutInflater)
            val rentPropositionId = arguments?.getInt(RENT_PROPOSITION_ID, -1)
            bindData(rentPropositionId)

            val builder = AlertDialog.Builder(it)

            builder
                .setCancelable(true)
                .setView(binding.root)
                .setPositiveButton("Tak") {dialog, _ ->
                    viewModel.addRentDecision(rentPropositionId!!, true) { result ->
                        if(result){
                            dialog.dismiss()
                        }
                    }

                }
                .setNegativeButton("Nie") { dialog, _ ->
                    viewModel.addRentDecision(rentPropositionId!!, false){ result ->
                        if(result){
                            dialog.dismiss()
                        }
                    }
                }

            val mAlertDialog : AlertDialog = builder.create()

            mAlertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun bindData(rentId: Int?) {
        viewModel.getRentProposition(rentId!!)

        viewModel.rent.observe(this) {
            val adapter = ProfileAdapter(it.tenants
            ) { _, _, position, _ ->
                val userId = it.tenants[position].userId
                TODO("Not yet implemented")
                //                    val bundle = Bundle()
                //                    bundle.putInt(USER_ID, userId)
                //                    val fragment = ProfileFragment()
                //                    fragment.arguments = bundle
                //                    (activity as? DrawerActivity)!!.replaceFragment(fragment)
            }

            binding.startDate.text = it.startDate
            binding.duration.text = it.duration.toString()
            binding.usersRecyclerView.adapter = adapter
            binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        }

    }

}