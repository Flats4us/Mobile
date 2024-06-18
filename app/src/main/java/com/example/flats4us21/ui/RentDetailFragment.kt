package com.example.flats4us21.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.adapters.PaymentAdapter
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.data.Rent
import com.example.flats4us21.databinding.FragmentRentDetailBinding
import com.example.flats4us21.viewmodels.RentViewModel
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "RentDetailFragment"
class RentDetailFragment : Fragment() {
    private var _binding : FragmentRentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : RentViewModel
    private lateinit var userViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rentId = arguments?.getInt(RENT_ID, -1)

        viewModel = ViewModelProvider(requireActivity())[RentViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModel.getRent(rentId!!)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if(isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.rent.observe(viewLifecycleOwner) { rent ->
            bindOfferData(rent)
        }

        binding.fab.setOnClickListener {
            showDialog(rentId)
        }

    }

    private fun bindOfferData(rent: Rent?) {
        rent ?: return

        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(rent.propertyImages)
        val imageCount = imageSlider.adapter?.itemCount ?: 0
        var imageText = "1/$imageCount"
        binding.imageCount.text = imageText
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentImage = position + 1
                imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })

        val url = "$URL/${rent.owner.profilePicture?.path}"
        Log.i(TAG, url)
        binding.ownerPhoto.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.owner.text = getString(R.string.name_and_surname, rent.owner.name, rent.owner.surname)
        binding.ownerLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(USER_ID, rent.owner.id)
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.startDate.text = rent.startDate.split("T")[0]
        binding.endDate.text = rent.startDate.split("T")[0]
        binding.duration.text = rent.duration.toString()
        binding.address.text = rent.propertyAddress

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
            { position ->
                val userId = rent.tenants[position].userId
                Log.i(TAG, "userId: $userId")
                val bundle = Bundle().apply {
                    putInt(USER_ID, userId)
                }
                val fragment = ReviewSubmissionFragment().apply {
                    arguments = bundle
                }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            },
            rent.isFinished
        )

        binding.rentRecyclerView.adapter = adapter
        binding.rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val paymentAdapter = PaymentAdapter(rent.payments)

        binding.paymentRecyclerView.adapter = paymentAdapter
        binding.paymentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showDialog(rentId: Int) {
        val rent = viewModel.rent.value ?: return
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_rent_layout)

        val layoutArgument = dialog.findViewById<View>(R.id.layoutArgument)
        val layoutAddOpinion = dialog.findViewById<View>(R.id.layoutAddOpinion)

        if(rent.isFinished && DataStoreManager.userRole.value == "Student" && userViewModel.myProfile.value!!.userId == rent.mainTenantId) {
            layoutAddOpinion.visibility = View.VISIBLE
        } else {
            layoutAddOpinion.visibility = View.GONE
        }
        if(rent.isFinished) {
            layoutArgument.visibility = View.GONE
        } else {
            layoutArgument.visibility = View.VISIBLE
        }

        layoutArgument.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RENT_ID, rentId)
            val fragment = AddArgumentFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }
        layoutAddOpinion.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RENT_ID, rentId)
            val fragment = AddPropertyOpinionFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}