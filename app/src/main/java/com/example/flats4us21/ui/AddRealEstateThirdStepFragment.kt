package com.example.flats4us21.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
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
                    Toast.makeText(requireContext(), "Możesz dodać maksymalnie 16 zdjęć!", Toast.LENGTH_SHORT).show()
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
            if(validateImages()) {
                collectData()
                (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFourthStepFragment())
            }
        }
    }

    private fun setImages() {
        selectedImageUris = realEstateViewModel.images

        lastIndexBeforeUpdate = if (selectedImageUris.size > 0) {
            selectedImageUris.size - 1
        } else {
            0
        }
    }

    private fun collectData() {
        realEstateViewModel.images = selectedImageUris
    }

    private fun validateImages(): Boolean {
        if(selectedImageUris.size > 0){
            binding.warning.isVisible = false
            binding.photoRecyclerView.isVisible = true
        } else {
            binding.warning.isVisible = true
            binding.photoRecyclerView.isVisible = false
        }
        return selectedImageUris.size > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}