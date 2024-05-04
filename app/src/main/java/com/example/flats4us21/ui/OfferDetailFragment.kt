package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Flat
import com.example.flats4us21.data.House
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Room
import com.example.flats4us21.databinding.FragmentOfferDetailBinding
import com.example.flats4us21.viewmodels.DetailOfferViewModel
import com.example.flats4us21.viewmodels.OfferViewModel
import java.time.Period

private const val TAG = "OfferDetailFragment"
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

        detailOfferViewModel.getOfferDetails(offerId!!)

        detailOfferViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        detailOfferViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
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
        binding.rent.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, offerId)
            val realEstateRentalDialogFragment = RealEstateRentalDialogFragment(detailOfferViewModel)
            realEstateRentalDialogFragment.arguments = bundle
            realEstateRentalDialogFragment.show(parentFragmentManager , "RealEstateRentalDialogFragment")

            detailOfferViewModel.rent.observe(viewLifecycleOwner) {rent ->
                if(rent != null){
                    //TODO: create request to api
                    Log.d("OfferDetailFragment", rent.toString())
                    Toast.makeText(requireContext(), "Zgłoszono wynajem.",Toast.LENGTH_LONG).show()
                    val fragment = SearchFragment()
                    (activity as? DrawerActivity)!!.replaceFragment(fragment)
                }
            }
        }
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        if(false){
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
        val url = "$URL/Images/Users/${offer.owner.profilePicture}"
        Log.i(TAG, url)
        binding.ownerPhoto.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.owner.text = "${offer.owner.name} ${offer.owner.surname}"

        binding.startDate.text = offer.dateIssue
        binding.endDate.text = offer.dateIssue
        binding.price.text = offer.price
        binding.city.text = offer.property.city
        binding.district.text = offer.property.district
        binding.street.text = " ${offer.property.street} ${offer.property.buildingNumber}"
        binding.area.text = offer.property.area.toString()
        binding.numberOfRooms.text = offer.property.numberOfRooms.toString()
        val period = Period.between(offer.startDate.toLocalDate(), offer.endDate.toLocalDate())
        val monthsBetween = period.years * 12 + period.months
        binding.period.text = monthsBetween.toString()
        binding.maxResidents.text = offer.property.maxNumberOfInhabitants.toString()
        binding.constructionYear.text = offer.property.constructionYear.toString()
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in offer.property.equipment.indices){
            stringBuilder.append(offer.property.equipment[j].equipmentName)

            if(j != offer.property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        if(offer.property.equipment.isEmpty()){
            stringBuilder.append("BRAK")
        }
        binding.equipment.text = stringBuilder.toString()
        binding.description.text = offer.description
        binding.interestedPeople.text = offer.interestedPeople.toString()

        when(offer.property){
            is House -> {
                val house: House = offer.property
                binding.landArea.text = house.landArea.toString()
                binding.landAreaLayout.visibility = View.VISIBLE
            }
            is Flat -> {}
            is Room -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}