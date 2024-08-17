package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.InterestAdapter
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentMyProfileBinding
import com.example.flats4us21.viewmodels.UserViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayout
import java.util.Locale

private const val TAG = "MyProfileFragment"
class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMyProfile {}

        viewModel.myProfile.observe(viewLifecycleOwner) { userProfile ->
            if (userProfile != null)
                bindData(userProfile)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.profileLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.personalDataLayout.visibility = View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        binding.buttonsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        binding.profileLayout.visibility = View.VISIBLE
                        binding.personalDataLayout.visibility = View.GONE
                    }
                    1 -> {
                        binding.profileLayout.visibility = View.GONE
                        binding.personalDataLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.editProfileButton.setOnClickListener {
            val fragment = EditProfileFragment()
            (activity as? DrawerActivity)?.replaceFragment(fragment)
        }

        binding.reviewsButton.setOnClickListener {
            val fragment = UserOpinionsFragment()
            (activity as? DrawerActivity)?.replaceFragment(fragment)
        }
    }

    private fun bindData(userProfile: MyProfile) {
        Log.i(TAG, "bindData: $userProfile")
        val url = "$URL/${userProfile.profilePicture?.path ?: ""}"
        Log.i(TAG, url)
        binding.profilePicture.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.verified.visibility = if (userProfile.verificationStatus == 0) View.VISIBLE else View.INVISIBLE

        binding.nameTextview.text = userProfile.name
        if(userProfile.userType == 0) {
            binding.ratingLayout.visibility = View.GONE
            binding.interestsLayout.visibility = View.GONE
            binding.opinionLayout.visibility = View.GONE
            binding.socialMediaLayout.visibility = View.GONE
            binding.emailLayout.visibility = View.VISIBLE
            binding.phoneNumberLayout.visibility = View.VISIBLE
            binding.surveyFlexboxLayout.visibility = View.GONE
        } else {
            binding.ratingLayout.visibility = View.VISIBLE
            binding.interestsLayout.visibility = View.VISIBLE
            binding.opinionLayout.visibility = View.VISIBLE
            binding.socialMediaLayout.visibility = View.VISIBLE
            binding.emailLayout.visibility = View.GONE
            binding.phoneNumberLayout.visibility = View.GONE
            binding.surveyFlexboxLayout.visibility = View.VISIBLE
        }
        binding.ratingBar.rating = userProfile.avgRating

        Log.i(TAG, "surveyStudent: ${userProfile.surveyStudent}")
        if(userProfile.surveyStudent != null) {
            val survey = userProfile.surveyStudent
            binding.smokingLayout.visibility = if(survey.smoking) View.VISIBLE else View.GONE
            Log.i(TAG, "smoking: ${survey.smoking}")
            binding.sociabilityLayout.visibility = if(survey.sociability) View.VISIBLE else View.GONE
            Log.i(TAG, "sociability: ${survey.sociability}")
            binding.animalsLayout.visibility = if(survey.animals) View.VISIBLE else View.GONE
            Log.i(TAG, "animals: ${survey.animals}")
            binding.veganLayout.visibility = if(survey.vegan) View.VISIBLE else View.GONE
            Log.i(TAG, "vegan: ${survey.vegan}")
        }

        if (userProfile.links != null) {
            binding.facebook.isVisible = userProfile.links.any { it.contains("facebook") }
            Log.i(TAG, "bindData: ${userProfile.links.any { it.contains("facebook") }}")
            binding.twitter.isVisible = userProfile.links.any { it.contains("twitter") }
            binding.instagram.isVisible = userProfile.links.any { it.contains("instagram") }
        } else {
            binding.socialMediaLayout.visibility = View.GONE
            binding.facebook.isVisible = false
            binding.twitter.isVisible = false
            binding.instagram.isVisible = false
        }

        if (userProfile.university != null) {
            binding.university.text = userProfile.university
            binding.studyLayout.visibility = View.VISIBLE
        } else {
            binding.studyLayout.visibility = View.GONE
        }

        if (userProfile.interests.isNullOrEmpty()) {
            binding.interestsRecyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            val recyclerView = binding.interestsRecyclerView
            binding.interestsRecyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            val interests = userProfile.interests.map { QuestionTranslator.translateInterestName(it.interestName.lowercase(Locale.getDefault()), requireContext()) }
            val adapter = InterestAdapter(interests)
            recyclerView.adapter = adapter
            val flexboxLayoutManager = FlexboxLayoutManager(context)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            recyclerView.layoutManager = flexboxLayoutManager
        }

        var hasReviews = false

        binding.layoutHelpful.visibility = if (userProfile.sumHelpful == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumHelpful.text = userProfile.sumHelpful.toString()

        binding.layoutCooperative.visibility = if (userProfile.sumCooperative == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumCooperative.text = userProfile.sumCooperative.toString()

        binding.layoutTidy.visibility = if (userProfile.sumTidy == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumTidy.text = userProfile.sumTidy.toString()

        binding.layoutFriendly.visibility = if (userProfile.sumFriendly == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumFriendly.text = userProfile.sumFriendly.toString()

        binding.layoutRespectingPrivacy.visibility = if (userProfile.sumRespectingPrivacy == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumRespectingPrivacy.text = userProfile.sumRespectingPrivacy.toString()

        binding.layoutCommunicative.visibility = if (userProfile.sumCommunicative == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumCommunicative.text = userProfile.sumCommunicative.toString()

        binding.layoutUnfair.visibility = if (userProfile.sumUnfair == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumUnfair.text = userProfile.sumUnfair.toString()

        binding.layoutLackOfHygiene.visibility = if (userProfile.sumLackOfHygiene == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumLackOfHygiene.text = userProfile.sumLackOfHygiene.toString()

        binding.layoutUntidy.visibility = if (userProfile.sumUntidy == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumUntidy.text = userProfile.sumUntidy.toString()

        binding.layoutConflicting.visibility = if (userProfile.sumConflicting == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumConflicting.text = userProfile.sumConflicting.toString()

        binding.layoutNoisy.visibility = if (userProfile.sumNoisy == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumNoisy.text = userProfile.sumNoisy.toString()

        binding.layoutNotFollowingTheArrangements.visibility = if (userProfile.sumNotFollowingTheArrangements == 0) {
            View.GONE
        } else {
            hasReviews = true
            View.VISIBLE
        }
        binding.sumNotFollowingTheArrangements.text = userProfile.sumNotFollowingTheArrangements.toString()

        binding.noReviewsText.visibility = if (hasReviews) View.GONE else View.VISIBLE

        binding.email.text = userProfile.email
        binding.phoneNumber.text = userProfile.phoneNumber
        if (userProfile.studentNumber != null) {
            binding.studentNumber.text = userProfile.studentNumber
            binding.studentNumberLayout.visibility = View.VISIBLE
        } else {
            binding.studentNumberLayout.visibility = View.GONE
        }
        if (userProfile.birthDate != null) {
            binding.birthDate.text = userProfile.birthDate.split("T")[0]
            binding.birthDateLayout.visibility = View.VISIBLE
        } else {
            binding.birthDateLayout.visibility = View.GONE
        }
        if (userProfile.bankAccount != null) {
            binding.bankAccount.text = userProfile.bankAccount
            binding.bankAccountLayout.visibility = View.VISIBLE
        } else {
            binding.bankAccountLayout.visibility = View.GONE
        }
        if (userProfile.documentNumber != null) {
            binding.documentNumber.text = userProfile.documentNumber
            binding.documentNumberLayout.visibility = View.VISIBLE
        } else {
            binding.documentNumberLayout.visibility = View.GONE
        }
        binding.documentExpireDate.text = userProfile.documentExpireDate.split("T")[0]

        binding.accountCreationDate.text = userProfile.accountCreationDate.split("T")[0]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
