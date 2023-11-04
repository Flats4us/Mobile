package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.R
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.FragmentOfferDetailBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import com.example.flats4us21.viewmodels.OfferViewModel


class OfferDetailFragment : Fragment() {
    private var _binding : FragmentOfferDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel
    private lateinit var detailOfferViewModel: DetailOfferViewModel
    private lateinit var addButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offerId = arguments?.getInt(OFFER_ID, -1)

        addButton = binding.addButton
        viewModel = ViewModelProvider(requireActivity())[OfferViewModel::class.java]
        detailOfferViewModel = ViewModelProvider(this)[DetailOfferViewModel::class.java]
        detailOfferViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        detailOfferViewModel.offer.observe(viewLifecycleOwner) { offer ->
            //TODO: viewModel.addOfferToLastViewed(offer)
            bindOfferData(offer)
            addButton.setOnClickListener {
                if (addButton.tag == true) {
                    addButton.setImageResource(R.drawable.unobserve)
                    addButton.tag = false
                    if (offer != null) {
                        //TODO: viewModel.unwatchOffer(offer)
                    }
                } else {
                    addButton.setImageResource(R.drawable.observe)
                    addButton.tag = true
                    if (offer != null) {
                        viewModel.watchOffer(offer)
                    }
                }
            }
        }
        detailOfferViewModel.getOfferDetails(offerId!!)
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        if(viewModel.checkIfIsWatched(offer)){
            binding.addButton.setImageResource(R.drawable.observe)
            binding.addButton.tag = true
        } else{
            binding.addButton.setImageResource(R.drawable.unobserve)
            binding.addButton.tag = false

        }
        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(offer.property.images)
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
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in offer.property.equipment.indices){
            stringBuilder.append(offer.property.equipment[j])

            if(j != offer.property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        binding.equipment.text = stringBuilder.toString()
        binding.area.text = offer.property.area.toString()
        binding.description.text = offer.description
        binding.interestedPeople.text = offer.interestedPeople.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}