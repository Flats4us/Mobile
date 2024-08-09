package com.example.flats4us21.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.R
import com.example.flats4us21.adapters.InterestAdapter
import com.example.flats4us21.data.UserType
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentRegisterBinding
import com.example.flats4us21.viewmodels.UserViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

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

        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val galleryUri = it
            try{
                binding.profilePicture.setImageURI(galleryUri)
                profilePicture = galleryUri
            }catch(e:Exception){
                e.printStackTrace()
            }
        }

        pickedInterest = mutableListOf()
        setVisibility()
        setValues()
        setupInterest()

        binding.editProfilePicture.setOnClickListener {
            getImageFromGallery(galleryLauncher)
        }

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

    private fun getImageFromGallery(galleryLauncher: ActivityResultLauncher<String>) {
        galleryLauncher.launch("image/*")
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

    private fun updateInterestRecyclerView() {
        if(pickedInterest.size == 0) {
            binding.recyclerView.visibility = View.GONE
            binding.interests.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.interests.visibility = View.GONE
            val equipment = userViewModel.interests.value!!
                .filter{ pickedInterest.contains(it.interestId)}
                .map { QuestionTranslator.translateInterestName(it.interestName.lowercase(Locale.getDefault()), requireContext() ) }
            val adapter = InterestAdapter(equipment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupInterest() {
        val interestButton = binding.addInterest
        val selectedInterests: MutableList<Int> = mutableListOf()

        userViewModel.getInterests()
        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        userViewModel.interests.observe(viewLifecycleOwner) { interests ->
            val interestArray = interests.map { QuestionTranslator.translateInterestName(it.interestName.lowercase(Locale.getDefault()), requireContext()) }.toTypedArray()
            val selectedInterestArray = BooleanArray(interestArray.size) { index ->
                userViewModel.interest.contains(index + 1)
            }

            interestButton.setOnClickListener {
                showInterestDialog(interestArray, selectedInterestArray, selectedInterests)
            }
        }
    }

    private fun showInterestDialog(interestArray: Array<String>, selectedInterestArray: BooleanArray, selectedInterests: MutableList<Int>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Wybierz zainteresowania")
            .setCancelable(false)
            .setMultiChoiceItems(interestArray, selectedInterestArray) { _, position, isChecked ->
                selectedInterestArray[position] = isChecked
                if (isChecked) {
                    selectedInterests.add(position)
                } else {
                    selectedInterests.remove(position)
                }
            }
            .setPositiveButton("Akceptuj") { _, _ ->
                updatePickedInterests(selectedInterestArray)
            }
            .setNegativeButton("Anuluj") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Wyczyść") { _, _ ->
                clearSelectedInterests(selectedInterestArray, selectedInterests)
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun updatePickedInterests(selectedInterestArray: BooleanArray) {
        for (j in selectedInterestArray.indices) {
            if (selectedInterestArray[j] && !pickedInterest.contains(j + 1)) {
                pickedInterest.add(j + 1)
            } else if (!selectedInterestArray[j] && pickedInterest.contains(j + 1)) {
                pickedInterest.remove(j + 1)
            }
        }
        updateInterestRecyclerView()
    }

    private fun clearSelectedInterests(selectedInterestArray: BooleanArray, selectedInterests: MutableList<Int>) {
        for (j in selectedInterestArray.indices) {
            selectedInterestArray[j] = false
        }
        selectedInterests.clear()
        binding.interests.text = ""
        pickedInterest.clear()
        updateInterestRecyclerView()
    }



    private fun setValues() {

        if (userViewModel.profilePicture != null) {
            binding.profilePicture.setImageURI(userViewModel.profilePicture)
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
        updateInterestRecyclerView()
        links.clear()
        links.addAll(userViewModel.links)
    }

    private fun collectData() {
        if(profilePicture != null){
            userViewModel.profilePicture = profilePicture
            val inputStream: InputStream? =
                requireContext().contentResolver.openInputStream(profilePicture!!)
            val mimeType = requireContext().contentResolver.getType(profilePicture!!)
            val fileExtension = when (mimeType) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                else -> ".jpg"
            }
            val file = File.createTempFile("temp_image", fileExtension, requireContext().cacheDir)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            userViewModel.profilePictureFile = file
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
