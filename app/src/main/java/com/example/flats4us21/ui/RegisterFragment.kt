package com.example.flats4us21.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentRegisterBinding
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private val links: MutableList<String> = mutableListOf()
    private lateinit var pickedInterest: MutableList<Int>
    private var profilePicture: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickedInterest = mutableListOf()
        setVisibility()
        setValues()
        setProfilePicturePicker()
        setupInterest()


        binding.layoutLinks.setOnClickListener{
            val linksDialogFragment = LinksDialogFragment(links)
            linksDialogFragment.show(parentFragmentManager , "LinksDialogFragment")
        }

        val prevButton = binding.prevButton
        prevButton.setOnClickListener {
            collectData()
            val fragment = RegisterSelectingUserTypeFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }

        val nextButton = binding.nextButton
        nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                val fragment = RegisterSpecificDataFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun setVisibility() {
        if(userViewModel.userType.toString() == UserType.OWNER.toString()){
            binding.layoutLinksWithHeader.visibility = View.GONE
            binding.layoutInterestsWithHeader.visibility = View.GONE
        } else if(userViewModel.userType.toString() == UserType.STUDENT.toString()){
            binding.layoutLinksWithHeader.visibility = View.VISIBLE
            binding.layoutInterestsWithHeader.visibility = View.VISIBLE
        }
    }

    private fun setupInterest() {
        val interest = binding.interests
        val interestList: MutableList<Int> = mutableListOf()
        userViewModel.getInterests()
        userViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
        userViewModel.interests.observe(viewLifecycleOwner) { interests ->
            val interestArray = interests.map {it.interestName }.toTypedArray()
            val selectedInterest = BooleanArray(interestArray.size) {index ->
                userViewModel.interest.contains(index+1)
            }

            interest.setOnClickListener{
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Wybierz zainteresowania")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(interestArray, selectedInterest){_, position, isChecked ->
                    selectedInterest[position] = isChecked

                    if(isChecked) {
                        interestList.add(position)
                        interestList.sort()
                    } else {
                        interestList.remove(position)
                    }
                }
                builder.setPositiveButton("Akceptuj") {_, _ ->
                    for (j in selectedInterest.indices) {
                        if (selectedInterest[j] && !pickedInterest.contains(j + 1)){
                            pickedInterest.add(j + 1)
                        } else if (!selectedInterest[j] && pickedInterest.contains(j +1)){
                            pickedInterest.remove(j + 1)
                        }
                    }
                }
                builder.setNegativeButton("Anuluj") {dialog, _ ->
                    dialog.dismiss()
                }
                builder.setNeutralButton("Wyczyść") {_, _ ->
                    for(j in selectedInterest.indices) {
                        selectedInterest[j] = false
                        interestList.clear()
                        interest.text = ""
                        pickedInterest.remove(j+1)
                    }
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    private fun setProfilePicturePicker() {
        var imageUri: Uri?
        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if(uri != null){
                    imageUri = uri
                    binding.profilePicture.setImageURI(imageUri)
                    profilePicture = imageUri
                }
            }
        binding.addProfilePictureButton.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setValues() {
        if(userViewModel.profilePicture != null){
            binding.profilePicture.setImageURI(userViewModel.profilePicture)
            profilePicture = userViewModel.profilePicture
        }
        if(userViewModel.name != ""){
            binding.name.setText(userViewModel.name)
        }
        if(userViewModel.surname != ""){
            binding.surname.setText(userViewModel.surname)
        }
        if(userViewModel.address != ""){
            binding.address.setText(userViewModel.address)
        }
        if(userViewModel.phoneNumber != ""){
            binding.phoneNumber.setText(userViewModel.phoneNumber)
        }
        if(userViewModel.links.size != 0){
            links.addAll(userViewModel.links)
        }
        pickedInterest.clear()
        pickedInterest.addAll(userViewModel.interest)
        links.clear()
        links.addAll(userViewModel.links)
    }

    private fun collectData() {
        if(profilePicture != null){
            userViewModel.profilePicture = profilePicture
        }
        if(!binding.name.text.isNullOrEmpty()) {
            userViewModel.name = binding.name.text.toString()
        }
        if(!binding.surname.text.isNullOrEmpty()){
            userViewModel.surname = binding.surname.text.toString()
        }
        if(!binding.address.text.isNullOrEmpty()){
            userViewModel.address = binding.address.text.toString()
        }
        if(!binding.phoneNumber.text.isNullOrEmpty()){
            userViewModel.phoneNumber = binding.phoneNumber.text.toString()
        }
        userViewModel.links = links
        userViewModel.interest = pickedInterest
    }

    private fun validateData(): Boolean {
        val isNameValid = validateOptionalText(binding.name, binding.layoutName, binding.layoutNameHeader)
        val isSurnameValid = validateOptionalText(binding.surname, binding.layoutSurname, binding.layoutSurnameHeader)
        val isCityValid = validateOptionalText(binding.address, binding.layoutAddress, binding.addressHeader)
        val isPhoneNumberValid = validateOptionalText(binding.phoneNumber, binding.layoutPhoneNumber, binding.phoneNumberHeader)

        return isNameValid && isSurnameValid && isCityValid && isPhoneNumberValid
    }

    private fun validateOptionalText(editText: EditText, editTextLayout : ViewGroup, header : TextView): Boolean {
        val text = editText.text.toString()
        val isRequired = header.text.last() == '*'
        val isValid = text.isNotEmpty()
        editTextLayout.setBackgroundResource(if (isValid || !isRequired) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid || !isRequired
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
