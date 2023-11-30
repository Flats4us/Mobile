package com.example.flats4us21.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.adapters.PhotoAdapter
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterAddDocumentBinding
import com.example.flats4us21.viewmodels.UserViewModel


class RegisterAddDocumentFragment : Fragment() {
    private var _binding : FragmentRegisterAddDocumentBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var photoAdapter: PhotoAdapter
    private var selectedImageUris : MutableList<Uri> = mutableListOf()
    private var lastIndexBeforeUpdate : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
       _binding = FragmentRegisterAddDocumentBinding.inflate(inflater, container, false)
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
                if(selectedImageUris.size > 0){
                    binding.warning.isVisible = false
                    binding.photoRecyclerView.isVisible = true
                }
            }

        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        photoAdapter = PhotoAdapter(selectedImageUris)
        recyclerView.adapter = photoAdapter

        binding.addPhotoButton.setOnClickListener {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.prevButton.setOnClickListener {
            collectData()
            var fragment: Fragment = SurveyFragment()
            if(userViewModel.userType == UserType.OWNER.toString())
                fragment = RegisterSpecificDataFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }
        binding.nextButton.setOnClickListener {
            if(validateImages()) {
                collectData()
                Toast.makeText(requireContext(), "Utworzono konto", Toast.LENGTH_SHORT).show()
                val fragment = LoginFragment()
                (activity as? DrawerActivity)!!.replaceFragment(fragment)

            }
        }
    }

    private fun setImages() {
        selectedImageUris = userViewModel.images

        lastIndexBeforeUpdate = if (selectedImageUris.size > 0) {
            selectedImageUris.size - 1
        } else {
            0
        }
    }

    private fun collectData() {
        userViewModel.images = selectedImageUris
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