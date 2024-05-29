package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.dto.NewUserOpinionDto
import com.example.flats4us21.databinding.FragmentReviewSubmissionScreenBinding
import com.example.flats4us21.viewmodels.UserViewModel

class ReviewSubmissionFragment : Fragment() {
    private var _binding: FragmentReviewSubmissionScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewSubmissionScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val userId = arguments?.getInt(USER_ID)
        userViewModel.getProfile(userId!!)

        userViewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                bindData(profile)
            }
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if(isLoading) View.GONE else View.VISIBLE
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        userViewModel.resultMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            (activity as? DrawerActivity)!!.goBack()
        }

        binding.addButton.setOnClickListener {
            collectData()
            userViewModel.addUserOpinion(userId) {
                if(it) {
                    (activity as? DrawerActivity)!!.goBack()
                }
            }
        }
    }

    private fun collectData() {
        val rating = binding.starRating.rating
        val helpful = binding.helpful.isChecked
        val cooperative = binding.cooperative.isChecked
        val tidy = binding.tidy.isChecked
        val friendly = binding.friendly.isChecked
        val respectingPrivacy = binding.respectingPrivacy.isChecked
        val communicative = binding.communicative.isChecked
        val unfair = binding.unfair.isChecked
        val lackOfHygiene = binding.lackOfHygiene.isChecked
        val untidy = binding.untidy.isChecked
        val conflicting = binding.conflicting.isChecked
        val noisy = binding.noisy.isChecked
        val notFollowingTheArrangements = binding.notFollowingTheArrangements.isChecked
        val description = binding.reviewEditText.text.toString()

        userViewModel.newUserOpinion = NewUserOpinionDto(
            rating.toInt(),
            helpful,
            cooperative,
            tidy,
            friendly,
            respectingPrivacy,
            communicative,
            unfair,
            lackOfHygiene,
            untidy,
            conflicting,
            noisy,
            notFollowingTheArrangements,
            description
        )

    }

    private fun bindData(profile: Profile) {
        val url = "$URL/${profile.profilePicture.path}"
        binding.profilePicture.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.userNameText.text = profile.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


