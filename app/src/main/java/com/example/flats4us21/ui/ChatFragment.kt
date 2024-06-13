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
import com.example.flats4us21.adapters.MessageAdapter
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.databinding.FragmentChatBinding
import com.example.flats4us21.viewmodels.ChatViewModel
import com.example.flats4us21.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "ChatFragment"

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private var fetchedMessages: MutableList<ChatMessage> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.startConnection()

        val chatId = arguments?.getInt(CHAT_ID)

        if(chatId != null) {
            viewModel.getChatParticipants(chatId)
            viewModel.getChatHistory(chatId)
        }

        viewModel.chatHistory.observe(viewLifecycleOwner) { chatHistory ->
            Log.d(TAG, "Observed chat history: $chatHistory")
            if (chatId != null) {
                bindData(chatId, chatHistory as MutableList<ChatMessage>)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.d(TAG, "Loading state: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Log.e(TAG, "Error: $errorMessage")
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                    addMessage(messageContent)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun bindData(chatId: Int, chatHistory: MutableList<ChatMessage>) {
        val userChats = viewModel.userChats.value
        if (userChats != null) {
            val otherUserName = userChats.firstOrNull { it.chatId == chatId }?.otherUsername
            binding.nameAndSurname.text = otherUserName

            if (!::recyclerView.isInitialized) {
                recyclerView = binding.chatRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
                    stackFromEnd = true
                }
            }

            fetchedMessages.clear()
            fetchedMessages.addAll(chatHistory)
            Log.d(TAG, "Fetched messages after update: $fetchedMessages")

            if (!::adapter.isInitialized) {
                val chatParticipants = viewModel.chatParticipants.value
                if (chatParticipants != null) {
                    adapter = MessageAdapter(requireContext(), fetchedMessages, chatParticipants)
                    recyclerView.adapter = adapter
                }
            } else {
                adapter.updateMessages(fetchedMessages)
            }

            recyclerView.scrollToPosition(fetchedMessages.size - 1)
        }
    }

    private fun addMessage(content: String) {
        val userChats = viewModel.userChats.value
        if (userChats != null) {
            val otherUserId = viewModel.chatParticipants.value!!
            viewModel.sendMessage(otherUserId, content) { isSuccess ->
                if (isSuccess) {
                    val chatParticipants = viewModel.chatParticipants.value
                    if (chatParticipants != null) {
                        val newMessage = ChatMessage(
                            chatMessageId = fetchedMessages.size + 1,
                            content = content,
                            dateTime = getCurrentDateTime(),
                            senderId = userViewModel.myProfile.value?.userId ?: 0
                        )
                        fetchedMessages.add(newMessage)
                        adapter.notifyItemInserted(fetchedMessages.size - 1)
                        recyclerView.scrollToPosition(fetchedMessages.size - 1)
                    }
                }
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Stop SignalR connection when the fragment is destroyed
        viewModel.stopConnection()
        _binding = null
    }
}
