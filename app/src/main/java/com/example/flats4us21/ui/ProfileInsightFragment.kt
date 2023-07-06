package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentProfileBinding


class ProfileInsightFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Przypisanie wartości do elementów interfejsu użytkownika
        val profilePicture = binding.profilePicture
        profilePicture.setImageResource(R.drawable.baseline_account_box_24)

//        val nameTextView = findViewById<TextView>(R.id.name_textview)
//        nameTextView.text = "Anna"
//
//        val ageTextView = findViewById<TextView>(R.id.age_textview)
//        ageTextView.text = "28"

        val star1 = binding.star1
        star1.setImageResource(R.drawable.baseline_star_24)

        val star2 = binding.star2
        star2.setImageResource(R.drawable.baseline_star_24)

        val star3 = binding.star3
        star3.setImageResource(R.drawable.baseline_star_24)

        val star4 = binding.star4
        star4.setImageResource(R.drawable.baseline_star_24)

        val star5 = binding.star5
        star5.setImageResource(R.drawable.baseline_star_outline_24)

        val badgeDrinking = binding.badgeDrinking
        badgeDrinking.setBackgroundResource(R.drawable.baseline_no_drinks_24)

        val badgeFacebook = binding.badgeFacebook
        badgeFacebook.setBackgroundResource(R.drawable.baseline_shower_24)

        val descriptionTextView = binding.descriptionTextview
        descriptionTextView.text =
            "Jestem osobą ożywczą i entuzjastyczną, która ...(CZYTAJ WIĘCEJ)"

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//        bottomNavigationView.selectedItemId = R.id.profile_picture
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}