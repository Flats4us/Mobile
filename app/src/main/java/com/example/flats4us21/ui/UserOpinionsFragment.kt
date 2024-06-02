package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.UserOpinionAdapter
import com.example.flats4us21.data.UserOpinion
import com.example.flats4us21.databinding.FragmentUserOpinionsBinding
import com.example.flats4us21.viewmodels.UserViewModel


class UserOpinionsFragment : Fragment() {
    private var _binding: FragmentUserOpinionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: UserOpinionAdapter
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentUserOpinionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getInt(USER_ID, -1)
        if (userId != null) {
            viewModel.getProfile(userId)
        } else {
            viewModel.getMyProfile()
        }

        viewModel.profile.observe(viewLifecycleOwner) { userProfile ->
            if(userProfile != null)
                bindData(userProfile.userOpinions!!)
        }

        viewModel.myProfile.observe(viewLifecycleOwner) { userProfile ->
            if(userProfile != null)
                bindData(userProfile.userOpinions!!)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.opinionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun bindData(userOpinions: List<UserOpinion>?) {
        recyclerview = binding.opinionsRecyclerView
        if (!userOpinions.isNullOrEmpty()) {
            recyclerview.visibility = View.VISIBLE
            binding.noReviewsText.visibility = View.GONE
            adapter = UserOpinionAdapter(userOpinions)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.noReviewsText.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}