package com.example.flats4us.ui

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
import com.example.flats4us.R
import com.example.flats4us.adapters.CoilImagesAdapter
import com.example.flats4us.adapters.PhotoAdapter
import com.example.flats4us.data.Image
import com.example.flats4us.databinding.FragmentAddRealEstateThirdStepBinding
import com.example.flats4us.viewmodels.RealEstateViewModel
import java.io.File


class AddRealEstateThirdStepFragment : Fragment() {
    private var _binding : FragmentAddRealEstateThirdStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var coilImageAdapter: CoilImagesAdapter
    private var selectedImageUris : MutableList<Uri> = mutableListOf()
    private var fetchedImages : MutableList<Image> = mutableListOf()
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
                    Toast.makeText(requireContext(),
                        getString(R.string.you_can_add_max_16_photos), Toast.LENGTH_SHORT).show()
                }
                if(selectedImageUris.size > 0){
                    binding.warning.isVisible = false
                    binding.photoRecyclerView.isVisible = true
                }
            }

        if(!realEstateViewModel.isCreating) {
            val oldIMagesRecyclerView = binding.oldPhotoRecyclerView
            oldIMagesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
            coilImageAdapter = CoilImagesAdapter(fetchedImages) {position ->
                val fileId = fetchedImages[position].name
                realEstateViewModel.imagesToDelete.add(fileId)
            }
            oldIMagesRecyclerView.adapter = coilImageAdapter
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
            (requireParentFragment() as AddRealEstateFragment).decreaseProgressBar()
        }
        binding.nextButton.setOnClickListener {
            if(validateImages()) {
                collectData()
                (requireParentFragment() as AddRealEstateFragment).replaceFragment(AddRealEstateFourthStepFragment())
                (requireParentFragment() as AddRealEstateFragment).increaseProgressBar()
            }
        }
    }

    private fun setImages() {
        selectedImageUris = realEstateViewModel.imagesURI
        if (!realEstateViewModel.isCreating) {
            fetchedImages = realEstateViewModel.images
        } else {
            binding.oldPhotoLayout.visibility = View.GONE
        }

        lastIndexBeforeUpdate = if (selectedImageUris.size > 0) {
            selectedImageUris.size - 1
        } else {
            0
        }
    }

    private fun collectData() {
        realEstateViewModel.imagesURI = selectedImageUris
        val files = mutableListOf<File>()
        for (uri in selectedImageUris) {
            getFileFromUri(uri)?.let { file ->
                files.add(file)
            }
        }
        realEstateViewModel.imageFiles = files
    }

    private fun validateImages(): Boolean {
        if(selectedImageUris.size > 0 || fetchedImages.size > 0){
            binding.warning.isVisible = false
            binding.photoRecyclerView.isVisible = true
        } else {
            binding.warning.isVisible = true
            binding.photoRecyclerView.isVisible = false
        }
        return selectedImageUris.size > 0 || fetchedImages.size > 0
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "temp_file_${System.currentTimeMillis()}"
            val mimeType = requireContext().contentResolver.getType(uri)
            val fileExtension = when (mimeType) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                else -> ".jpg"
            }
            val tempFile = File.createTempFile(fileName, fileExtension, requireContext().cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}