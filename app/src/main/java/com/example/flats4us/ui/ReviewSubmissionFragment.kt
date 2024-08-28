package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Profile
import com.example.flats4us.data.dto.NewUserOpinionDto
import com.example.flats4us.data.utils.Constants.Companion.USER_ID
import com.example.flats4us.databinding.FragmentReviewSubmissionScreenBinding
import com.example.flats4us.viewmodels.UserViewModel

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

        val userId = arguments?.getInt(USER_ID) ?: return

        setupObservers()
        setupButtons(userId)

        userViewModel.getProfile(userId)
    }

    private fun setupObservers() {
        userViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let { bindData(it) }
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                userViewModel.clearErrorMessage()
            }
        }
    }

    private fun setupButtons(userId: Int) {
        binding.cancelButton.setOnClickListener {
            (activity as? DrawerActivity)?.goBack()
        }

        binding.addButton.setOnClickListener {
            collectData()
            userViewModel.addUserOpinion(userId) { success ->
                if (success) {
                    Toast.makeText(requireContext(), getString(R.string.added_opinion), Toast.LENGTH_LONG).show()
                    (activity as? DrawerActivity)?.goBack()
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
