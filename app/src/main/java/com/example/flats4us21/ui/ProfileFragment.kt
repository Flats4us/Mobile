package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.InterestAdapter
import com.example.flats4us21.data.Profile
import com.example.flats4us21.databinding.FragmentProfileBinding
import com.example.flats4us21.viewmodels.UserViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getInt(USER_ID, -1)
        viewModel.getProfile(userId!!)

        viewModel.profile.observe(viewLifecycleOwner) { userProfile ->
            if(userProfile != null)
                bindData(userProfile)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        binding.reviewsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(USER_ID, userId)
            val fragment = UserOpinionsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

    }

    private fun bindData(userProfile: Profile) {
        Log.i(TAG, "bindData: $userProfile")
        val url = "$URL/${userProfile.profilePicture.path}"
        binding.profilePicture.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.verified.visibility = if (userProfile.verificationStatus == 0) View.VISIBLE else View.INVISIBLE

        binding.nameTextview.text = userProfile.name
        if(userProfile.age != null){
            binding.ageTextview.visibility = View.VISIBLE
            binding.ageTextview.text = userProfile.age.toString()
        } else {
            binding.ageTextview.visibility = View.GONE
        }
        binding.ratingBar.rating = userProfile.avgRating
        if(userProfile.links != null){
            binding.facebook.isVisible = userProfile.links.any {it.contains("facebook")}
            binding.twitter.isVisible = userProfile.links.any {it.contains("twitter")}
            binding.instagram.isVisible = userProfile.links.any {it.contains("instagram")}
        } else {
            binding.facebook.isVisible = false
            binding.twitter.isVisible = false
            binding.instagram.isVisible = false
        }

        if(userProfile.university != null) {
            binding.university.text = userProfile.university
            binding.studyLayout.visibility = View.VISIBLE
        } else {
            binding.studyLayout.visibility = View.GONE
        }

        if(userProfile.interests.isNullOrEmpty()){
            binding.interestsRecyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            val recyclerView = binding.interestsRecyclerView
            binding.interestsRecyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            val adapter = InterestAdapter(userProfile.interests)
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}