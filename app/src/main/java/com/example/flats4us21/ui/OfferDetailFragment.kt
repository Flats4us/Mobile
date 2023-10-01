package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.R
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.FragmentOfferDetailBinding
import com.example.flats4us21.viewmodels.MainViewModel


class OfferDetailFragment : Fragment() {
    private var _binding : FragmentOfferDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val offer = viewModel.getOffer()
        bindOfferData(offer)

        val addButton = binding.addButton

        addButton.tag = false
        addButton.setOnClickListener {
            if(addButton.tag == true){
                addButton.setImageResource(R.drawable.unobserve)
                addButton.tag = false
            } else {
            addButton.setImageResource(R.drawable.observe)
                addButton.tag = true
            }
        }
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(offer.property.image)
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCount = imageSlider.adapter?.itemCount ?: 0
                val currentImage = position + 1
                val imageText = "Image $currentImage of $imageCount"
                binding.imageCount.text = imageText
            }
        })
        binding.dateIssue.text = offer.dateIssue
        binding.price.text = offer.price
        binding.address.text = "${offer.property.city} ${offer.property.street}"
        binding.period.text = offer.rentalPeriod
        binding.maxResidents.text = offer.property.maxResidents.toString()
        binding.equipment.text = offer.property.equipment
        binding.area.text = offer.property.area.toString()
        binding.description.text = offer.description
        binding.interestedPeople.text = offer.interestedPeople.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}