package com.example.flats4us21.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.flats4us21.databinding.FragmentEditProfileBinding
import android.app.DatePickerDialog
import java.util.Calendar


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val PICK_IMAGE_REQUEST = 1
    private val interests = mutableSetOf<String>()
    private val socialMediaLinks = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInterests.setOnClickListener { showInterestsDialog() }
        binding.textInterests.setOnLongClickListener {
            showRemoveInterestDialog()
            true
        }
        binding.profilePicture.setOnClickListener { changeProfilePicture() }
        binding.textSocialMediaLinks.setOnClickListener { showSocialMediaLinksDialog() }
        binding.textSocialMediaLinks.setOnLongClickListener {
            showRemoveSocialMediaLinkDialog()
            true
        }
        binding.layoutDate.setOnClickListener { showDatePickerDialog() }

    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            binding.textDate.text = selectedDate
        }, year, month, day).show()
    }


    private fun showInterestsDialog() {
        val interestsArray = arrayOf("Muzyka", "Sport", "Szutka", "Technologia")
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Wybierz zainteresowania")
            setItems(interestsArray) { _, which ->
                addInterestAsTag(interestsArray[which])
            }
            show()
        }
    }

    private fun addInterestAsTag(interest: String) {
        interests.add(interest)
        updateInterestsDisplay()
    }

    private fun removeInterest(interest: String) {
        interests.remove(interest)
        updateInterestsDisplay()
    }

    private fun updateInterestsDisplay() {
        binding.textInterests.text = interests.joinToString(", ")
    }

    private fun showRemoveInterestDialog() {
        val items = interests.toTypedArray()
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Usuń zainteresowanie")
            setItems(items) { _, which ->
                removeInterest(items[which])
            }
            show()
        }
    }

    private fun showSocialMediaLinksDialog() {
        // Custom input dialog for adding a new social media link
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        input.hint = "Wpisz link do mediów społecznościowych"

        AlertDialog.Builder(requireContext()).apply {
            setTitle("Dodaj link do mediów społecznościowych")
            setView(input)
            setPositiveButton("Dodaj") { _, _ ->
                addSocialMediaLink(input.text.toString())
            }
            setNegativeButton("Anuluj", null)
            show()
        }
    }

    private fun addSocialMediaLink(link: String) {
        if (link.isNotEmpty()) {
            socialMediaLinks.add(link)
            updateSocialMediaLinksDisplay()
        }
    }

    private fun removeSocialMediaLink(link: String) {
        socialMediaLinks.remove(link)
        updateSocialMediaLinksDisplay()
    }

    private fun updateSocialMediaLinksDisplay() {
        binding.textSocialMediaLinks.text = socialMediaLinks.joinToString(", ")
    }

    private fun showRemoveSocialMediaLinkDialog() {
        val items = socialMediaLinks.toTypedArray()
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Usuń link do mediów społecznościowych")
            setItems(items) { _, which ->
                removeSocialMediaLink(items[which])
            }
            show()
        }
    }

    private fun changeProfilePicture() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            binding.profilePicture.setImageURI(selectedImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
