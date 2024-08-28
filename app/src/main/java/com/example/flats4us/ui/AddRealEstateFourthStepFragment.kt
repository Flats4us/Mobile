package com.example.flats4us.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.adapters.NewImageSliderAdapter
import com.example.flats4us.data.PropertyType
import com.example.flats4us.data.utils.QuestionTranslator
import com.example.flats4us.databinding.FragmentAddRealEstateFourthStepBinding
import com.example.flats4us.viewmodels.RealEstateViewModel
import java.util.Locale

private const val TAG = "AddRealEstateFourthStepFragment"
class AddRealEstateFourthStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateFourthStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateFourthStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        setListeners()

        realEstateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            setButtonsEnabled(!isLoading)
        }

        realEstateViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                realEstateViewModel.clearErrorMessage()
            }
        }
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        with(binding) {
            prevButton.isEnabled = enabled
            addPropertyButton.isEnabled = enabled
            updatePropertyButton.isEnabled = enabled
            resetButton.isEnabled = enabled
        }
    }

    private fun setListeners() {
        binding.prevButton.setOnClickListener {
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateThirdStepFragment())
            (requireParentFragment() as AddRealEstateFragment).decreaseProgressBar()
        }
        binding.addPropertyButton.setOnClickListener {
            realEstateViewModel.createProperty { result ->
                if (result) {
                    performAction()
                }

            }
        }
        binding.updatePropertyButton.setOnClickListener {
            realEstateViewModel.updateProperty() {
                if (it) {
                    performAction()
                }
            }
        }
        binding.resetButton.setOnClickListener {
            reset()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFirstStepFragment())
            (requireParentFragment() as AddRealEstateFragment).decreaseProgressBar(-75)
        }
    }

    private fun performAction() {
        if(realEstateViewModel.errorMessage.value == null){
            val message = if (realEstateViewModel.isCreating) getString(R.string.success_created_property) else getString(R.string.success_updated_property)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            val fragment = OwnerPropertiesFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
            reset()
        }
    }

    private fun bindData() {
        val imageSlider = binding.image
        imageSlider.adapter = NewImageSliderAdapter(realEstateViewModel.images, urisToBitmaps(requireContext(), realEstateViewModel.imagesURI))
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCount = imageSlider.adapter?.itemCount ?: 0
                val currentImage = position + 1
                val imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })
        if(realEstateViewModel.isCreating){
            binding.addPropertyButton.visibility = View.VISIBLE
            binding.updatePropertyButton.visibility = View.GONE
        } else {
            binding.addPropertyButton.visibility = View.GONE
            binding.updatePropertyButton.visibility = View.VISIBLE
        }
        binding.voivodeship.text = realEstateViewModel.voivodeship
        binding.city.text = realEstateViewModel.city
        if(realEstateViewModel.getDistricts(realEstateViewModel.city).size != 0){
            binding.district.text = realEstateViewModel.district
            binding.layoutDistrict.isVisible = true
        } else {
            binding.layoutDistrict.isVisible = false
        }
        binding.street.text = realEstateViewModel.street
        binding.buildingNumber.text = realEstateViewModel.buildingNumber
        if(realEstateViewModel.propertyType == PropertyType.FLAT){
            binding.floor.text = realEstateViewModel.floor.toString()
            binding.layoutFloor.isVisible = true
        } else {
            binding.layoutFloor.isVisible = false
        }
        if(realEstateViewModel.propertyType == PropertyType.FLAT){
            binding.flatNumber.text = realEstateViewModel.flatNumber
            binding.layoutFlatNumber.isVisible = true
        } else {
            binding.layoutFlatNumber.isVisible = false
        }
        binding.area.text = realEstateViewModel.area.toString()

        binding.maxResidents.text = realEstateViewModel.maxResidents.toString()
        if(realEstateViewModel.propertyType == PropertyType.HOUSE){
            binding.landArea.text = realEstateViewModel.landArea.toString()
            binding.layoutLandArea.isVisible = true
        } else {
            binding.layoutLandArea.isVisible = false
        }
        binding.constructionYear.text = realEstateViewModel.constructionYear.toString()
        if(realEstateViewModel.propertyType != PropertyType.ROOM){
            binding.numberOfRooms.text = realEstateViewModel.numberOfRooms.toString()
            binding.layoutNumberOfRooms.isVisible = true
        } else {
            binding.layoutNumberOfRooms.isVisible = false
        }
        if(realEstateViewModel.propertyType != PropertyType.ROOM){
            binding.numberOfFloors.text = realEstateViewModel.numberOfFloors.toString()
            binding.layoutNumberOfFloors.isVisible = true
        } else {
            binding.layoutNumberOfFloors.isVisible = false
        }
        val stringBuilder: StringBuilder = StringBuilder()
        for(j in realEstateViewModel.equipment.indices){
            stringBuilder.append(QuestionTranslator.translateEquipmentName(realEstateViewModel.equipments.value!![j].equipmentName.lowercase(Locale.getDefault()), requireContext()))

            if(j != realEstateViewModel.equipment.size-1){
                stringBuilder.append(", ")
            }
        }
        binding.equipment.text = stringBuilder.toString()
    }

    private fun reset() {
        realEstateViewModel.propertyType = null
        realEstateViewModel.voivodeship = ""
        realEstateViewModel.city = ""
        realEstateViewModel.district = ""
        realEstateViewModel.street = ""
        realEstateViewModel.buildingNumber = ""
        realEstateViewModel.postalCode = ""
        realEstateViewModel.area = 0
        realEstateViewModel.maxResidents = 0
        realEstateViewModel.constructionYear = 0
        realEstateViewModel.numberOfRooms = 0
        realEstateViewModel.numberOfFloors = 0
        realEstateViewModel.equipment = mutableListOf()
        realEstateViewModel.imagesURI.clear()
        realEstateViewModel.images.clear()
    }

    private fun urisToBitmaps(context: Context, uriList: List<Uri>): List<Bitmap> {
        val bitmapList = mutableListOf<Bitmap>()

        for (uri in uriList) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap != null) {
                    bitmapList.add(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bitmapList
    }
}