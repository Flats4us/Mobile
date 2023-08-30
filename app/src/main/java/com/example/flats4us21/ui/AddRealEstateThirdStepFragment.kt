package com.example.flats4us21.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flats4us21.adapters.PhotoAdapter
import com.example.flats4us21.databinding.FragmentAddRealEstateThirdStepBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel


class AddRealEstateThirdStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateThirdStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var photoAdapter: PhotoAdapter
    private var selectedImageUris : MutableList<Uri> = mutableListOf()
    private var lastIndexBeforeUpdate : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateThirdStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImages()

        val multiplePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(15)) { uris ->
                val remainingSpace = 16 - selectedImageUris.size
                if (remainingSpace > 0) {
                    val urisToAdd = uris.take(remainingSpace)
                    selectedImageUris.addAll(urisToAdd)

                    val startIndex = lastIndexBeforeUpdate
                    val endIndex = lastIndexBeforeUpdate + urisToAdd.size
                    photoAdapter.notifyItemRangeInserted(startIndex, endIndex)
                    lastIndexBeforeUpdate = endIndex
                } else {
                    Toast.makeText(requireContext(), "You've reached the maximum limit of 15 images.", Toast.LENGTH_SHORT).show()
                }
            }

        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        photoAdapter = PhotoAdapter(selectedImageUris)
        recyclerView.adapter = photoAdapter

        binding.addPhotoButton.setOnClickListener {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.prevButton.setOnClickListener {
            collectData()
            (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateSecondStepFragment())
        }
        binding.nextButton.setOnClickListener {
            collectData()
            //(requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFourthStepFragment())
        }
    }

    private fun setImages(){
        selectedImageUris = realEstateViewModel.imageUris
        lastIndexBeforeUpdate = if (selectedImageUris.size > 0) {
            selectedImageUris.size - 1
        } else {
            0
        }
    }

    private fun collectData() {
        realEstateViewModel.imageUris = selectedImageUris
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}