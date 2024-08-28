package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us.adapters.UserOpinionAdapter
import com.example.flats4us.data.UserOpinion
import com.example.flats4us.data.utils.Constants.Companion.USER_ID
import com.example.flats4us.databinding.FragmentUserOpinionsBinding
import com.example.flats4us.viewmodels.UserViewModel

class UserOpinionsFragment : Fragment() {
    private var _binding: FragmentUserOpinionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserOpinionsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val userId = arguments?.getInt(USER_ID, -1)
        if (userId != null && userId != -1) {
            viewModel.getProfile(userId)
        } else {
            viewModel.getMyProfile(){}
        }
    }

    private fun setupObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.userOpinions?.let { bindData(it) }
        }

        viewModel.myProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.userOpinions?.let { bindData(it) }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.opinionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
    }

    private fun bindData(userOpinions: List<UserOpinion>) {
        if (userOpinions.isNotEmpty()) {
            binding.noReviewsText.visibility = View.GONE
            binding.opinionsRecyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context)
                adapter = UserOpinionAdapter(userOpinions)
            }
        } else {
            binding.noReviewsText.visibility = View.VISIBLE
            binding.opinionsRecyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
