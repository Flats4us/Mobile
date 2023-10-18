package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentProfileBinding
import com.example.flats4us21.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userProfile = viewModel.getUserProfile()

        binding.profilePicture.setImageResource(userProfile.profilePictureResId)
        binding.nameTextview.text = "${userProfile.firstName} ${userProfile.lastName}"
        binding.ageTextview.text = userProfile.age.toString()
        binding.bioTextView.text = userProfile.bio

        // Ustawienie gwiazdek na podstawie oceny
        // In onViewCreated:
        val rating = userProfile.rating

        binding.star1.setImageResource(if (rating >= 1) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24)
        binding.star2.setImageResource(if (rating >= 2) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24)
        binding.star3.setImageResource(if (rating >= 3) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24)
        binding.star4.setImageResource(if (rating >= 4) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24)
        binding.star5.setImageResource(if (rating >= 5) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24)



        // Opcjonalnie: Ustawienie ikonek na podstawie atrybutów użytkownika (jeśli są dynamiczne)
        // Przykład:
        // if (userProfile.drinksAlcohol) {
        //     binding.badgeDrinking.setImageResource(R.drawable.baseline_drinks_24)
        // } else {
        //     binding.badgeDrinking.setImageResource(R.drawable.baseline_no_drinks_24)
        // }

        binding.editProfileButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, EditProfileFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
