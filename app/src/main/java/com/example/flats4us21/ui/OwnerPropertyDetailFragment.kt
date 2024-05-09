package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.data.Flat
import com.example.flats4us21.data.House
import com.example.flats4us21.data.Room
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.databinding.FragmentOwnerPropertyDetailBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val IS_CREATING = "IS_CREATING"
const val PROPERTY_ID = "PROPERTY_ID"
private const val TAG = "OwnerPropertyDetailFragment"
class OwnerPropertyDetailFragment : Fragment() {
    private var _binding : FragmentOwnerPropertyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerPropertyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        bindData(realEstateViewModel.selectedProperty!!)

        binding.fab.setOnClickListener {
            showPopup(binding.fab)
        }
    }

    private fun showPopup(fab: FloatingActionButton) {
        val popupMenu = PopupMenu(requireContext(), fab)
        popupMenu.inflate(R.menu.my_property_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.edit -> {
                    val bundle = Bundle()
                    bundle.putBoolean(IS_CREATING, false)
                    bundle.putInt(PROPERTY_ID, realEstateViewModel.selectedProperty!!.propertyId)
                    val fragment = AddRealEstateFragment()
                    fragment.arguments = bundle
                    (activity as? DrawerActivity)!!.replaceFragment(fragment)
                }

                R.id.delete -> {
                    realEstateViewModel.deleteProperty(realEstateViewModel.selectedProperty!!.propertyId) { result ->
                        result.let {
                            Log.e(TAG, result.toString())
                            val fragment = OwnerPropertiesFragment()
                            (activity as? DrawerActivity)!!.replaceFragment(fragment)
                        }
                    }
                }
            }

            true
        }

        popupMenu.show()
    }

    private fun bindData(property: Property) {
        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(property.images)
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCount = imageSlider.adapter?.itemCount ?: 0
                val currentImage = position + 1
                val imageText = "Image $currentImage of $imageCount"
                binding.imageCount.text = imageText
            }
        })
        binding.voivodeship.text = property.voivodeship
        binding.city.text = property.city
        if(property.district != null) {
            binding.district.text = realEstateViewModel.district
            binding.layoutDistrict.isVisible = true
        } else {
            binding.layoutDistrict.isVisible = false
        }
        binding.street.text = property.street
        binding.buildingNumber.text = property.buildingNumber
        binding.area.text = property.area.toString()
        binding.maxResidents.text = property.maxNumberOfInhabitants.toString()
        binding.constructionYear.text = property.constructionYear.toString()
        binding.numberOfRooms.text = property.numberOfRooms.toString()
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in property.equipment.indices){
            stringBuilder.append(property.equipment[j].equipmentName)
            if(j != property.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        binding.equipment.text = stringBuilder.toString()

        binding.layoutLandArea.isVisible = false
        binding.layoutFloor.isVisible = false
        binding.layoutFlatNumber.isVisible = false
        binding.layoutNumberOfFloors.isVisible = false
        when(property){
            is House -> {
                val house: House = property
                binding.landArea.text = house.landArea.toString()
                binding.layoutLandArea.isVisible = true
                binding.numberOfFloors.text = house.numberOfFloors.toString()
                binding.layoutNumberOfFloors.isVisible = true
            }
            is Flat -> {
                binding.floor.text = property.floor.toString()
                binding.layoutFloor.isVisible = true
                binding.flatNumber.text = property.flatNumber.toString()
                binding.layoutFlatNumber.isVisible = true
            }
            is Room -> {
                binding.layoutNumberOfRooms.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}