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
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.data.Rent
import com.example.flats4us21.databinding.FragmentRentDetailBinding
import com.example.flats4us21.viewmodels.RentViewModel

private const val TAG = "RentDetailFragment"
class RentDetailFragment : Fragment() {
    private var _binding : FragmentRentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : RentViewModel

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

        viewModel.getRent(rentId!!)

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.rent.observe(viewLifecycleOwner) { rent ->
            bindOfferData(rent)
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
        binding.startDate.text = rent.startDate.split("T")[0]
        binding.endDate.text = rent.startDate.split("T")[0]
        binding.duration.text = rent.duration.toString()
        binding.address.text = rent.propertyAddress

        val adapter = ProfileAdapter(
            rent.tenants
        ) { _, _, position, _ ->
            val userId = rent.tenants[position].userId
            Log.i(TAG, "userId: $userId")
            val bundle = Bundle()
            bundle.putInt(USER_ID, userId)
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.rentRecyclerView.adapter = adapter
        binding.rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}