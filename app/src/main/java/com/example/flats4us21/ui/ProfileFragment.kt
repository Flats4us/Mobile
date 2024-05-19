package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.InterestAdapter
import com.example.flats4us21.data.Interest
import com.example.flats4us21.data.MyProfile
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
        binding.reviewsPer.text = if (userProfile.avgRating.toInt() != 0) {
            (userProfile.avgRating / 5).toString()
        } else {
            "0"
        }
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

        binding.sumHelpful.text = userProfile.sumHelpful.toString()
        binding.sumCooperative.text = userProfile.sumCooperative.toString()
        binding.sumTidy.text = userProfile.sumTidy.toString()
        binding.sumFriendly.text = userProfile.sumFriendly.toString()
        binding.sumRespectingPrivacy.text = userProfile.sumRespectingPrivacy.toString()
        binding.sumCommunicative.text = userProfile.sumCommunicative.toString()
        binding.sumUnfair.text = userProfile.sumUnfair.toString()
        binding.sumLackOfHygiene.text = userProfile.sumLackOfHygiene.toString()
        binding.sumUntidy.text = userProfile.sumUntidy.toString()
        binding.sumConflicting.text = userProfile.sumConflicting.toString()
        binding.sumNoisy.text = userProfile.sumNoisy.toString()
        binding.sumNotFollowingTheArrangements.text = userProfile.sumNotFollowingTheArrangements.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}