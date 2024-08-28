package com.example.flats4us.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us.DrawerActivity
import com.example.flats4us.adapters.ChatAdapter
import com.example.flats4us.data.utils.Constants.Companion.CHAT_ID
import com.example.flats4us.data.utils.Constants.Companion.IS_CREATING
import com.example.flats4us.data.utils.Constants.Companion.USER_ID
import com.example.flats4us.databinding.FragmentChatsBinding
import com.example.flats4us.viewmodels.ChatViewModel

private const val TAG = "ChatsFragment"

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.getUserChats()
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(mutableListOf()) { selectedChat ->
            val bundle = Bundle()
            bundle.putInt(CHAT_ID, selectedChat.chatId)
            bundle.putInt(USER_ID, selectedChat.otherUserId)
            bundle.putBoolean(IS_CREATING, false)
            val fragment = ChatFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ChatsFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.userChats.observe(viewLifecycleOwner) { chats ->
            Log.d(TAG, "Observed user chats: ${chats.size}")
            adapter.updateChats(chats)
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
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.chatsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
