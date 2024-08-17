package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.MessageAdapter
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.data.Profile
import com.example.flats4us21.databinding.FragmentChatBinding
import com.example.flats4us21.viewmodels.ChatViewModel
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "ChatFragment"
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private var fetchedMessages: MutableList<ChatMessage> = mutableListOf()
    private var chatId: Int = 0
    private var userId: Int = 0
    private var isCreating: Boolean = false
    private var isAdapterInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startConnection()

        chatId = arguments?.getInt(CHAT_ID) ?:0
        userId = arguments?.getInt(USER_ID) ?: 0
        isCreating = arguments?.getBoolean(IS_CREATING) ?: false

        if(chatId != 0 || isCreating) {
            if (!isCreating) {
                userViewModel.getProfile(userId)
                viewModel.getChatHistory(chatId)
            } else {
                userViewModel.getProfile(userId)
            }
        }
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        recyclerView = binding.chatRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageAdapter(requireContext(), fetchedMessages, userViewModel.myProfile.value?.userId ?: 0, userViewModel.profile.value!!.profilePicture)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(fetchedMessages.size - 1)
        isAdapterInitialized = true
    }

    private fun setupListeners() {
        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                viewModel.sendMessage(userViewModel.myProfile.value?.userId ?: 0, userId, messageContent)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun setupObservers() {
        userViewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                Log.d(TAG, "Observed profile: $profile")
                bindHeaderData(profile)
                setupRecyclerView()
            }
        }

        viewModel.chatHistory.observe(viewLifecycleOwner) { chatHistory ->
            Log.d(TAG, "Observed chat history: $chatHistory")
            bindMessageData(chatHistory as MutableList<ChatMessage>)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.d(TAG, "Loading state: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
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

    private fun bindHeaderData(profile: Profile?) {
        profile ?: return

        val url = "$URL/${profile.profilePicture.path}"
        Log.i(TAG, url)
        binding.profilePicture.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.nameAndSurname.text = profile.name
    }

    private fun bindMessageData(chatHistory: MutableList<ChatMessage>) {
        fetchedMessages.clear()
        fetchedMessages.addAll(chatHistory)
        Log.d(TAG, "Fetched messages after update: $fetchedMessages")

        if(isAdapterInitialized) {
            adapter.updateMessages(fetchedMessages)
            recyclerView.scrollToPosition(fetchedMessages.size - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.stopConnection()
        _binding = null
    }
}
