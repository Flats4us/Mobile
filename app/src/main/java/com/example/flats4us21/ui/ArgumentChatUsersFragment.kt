package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.data.GroupChatUser
import com.example.flats4us21.data.Tenant
import com.example.flats4us21.databinding.FragmentArgumentChatUsersBinding
import com.example.flats4us21.viewmodels.ChatViewModel

private const val TAG = "ArgumentChatUsersFragment"
class ArgumentChatUsersFragment : Fragment() {
    private var _binding : FragmentArgumentChatUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        _binding = FragmentArgumentChatUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
    }

    private fun bindData() {
        val profiles = viewModel.groupChatInfo.value!!.users
        val adapter = ProfileAdapter(
            mapGroupChatUsersToTenants(profiles),
            { position ->
                val userId = profiles[position].userId
                Log.i(TAG, "userId: $userId")
                val bundle = Bundle().apply {
                    putInt(USER_ID, userId)
                }
                val fragment = ProfileFragment().apply {
                    arguments = bundle
                }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            },
            { position ->
                val userId = profiles[position].userId
                Log.i(TAG, "userId: $userId")
                val bundle = Bundle().apply {
                    putInt(USER_ID, userId)
                }
                val fragment = ReviewSubmissionFragment().apply {
                    arguments = bundle
                }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            },
            false
        )

        binding.userRecyclerView.adapter = adapter
        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun mapGroupChatUsersToTenants(users: List<GroupChatUser>?) : List<Tenant> {
        return users!!.map {
            Tenant(it.userId, it.email, it.fullName, it.profilePicture)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}