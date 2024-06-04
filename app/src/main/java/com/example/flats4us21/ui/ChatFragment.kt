package com.example.flats4us21.ui

import MessageAdapter
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
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.databinding.FragmentChatBinding
import com.example.flats4us21.viewmodels.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "ChatFragment"

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private var fetchedMessages: MutableList<ChatMessage> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatId = arguments?.getInt(CHAT_ID)

        if(chatId != null) {
            viewModel.getChatParticipants(chatId)
            viewModel.getChatHistory(chatId)
        }

        viewModel.chatMessages.observe(viewLifecycleOwner) { chatHistory ->
            bindData(chatId, chatHistory)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mainLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                addMessage(chatId!!, messageContent)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun bindData(chatId: Int?, chatHistory: MutableList<ChatMessage>) {
        val otherUserName = viewModel.chats.value!!.filter { it.chatId == chatId }[0].otherUsername
        binding.nameAndSurname.text = otherUserName

        if (!::recyclerView.isInitialized) {
            recyclerView = binding.chatRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
        }

        fetchedMessages.clear()
        fetchedMessages.addAll(chatHistory)

        if (!::adapter.isInitialized) {
            adapter = MessageAdapter(requireContext(), fetchedMessages, viewModel.chatParticipants.value!!)
            recyclerView.adapter = adapter
        } else {
            adapter.notifyDataSetChanged()
        }

        recyclerView.scrollToPosition(fetchedMessages.size - 1)
    }

    private fun addMessage(chatId: Int, content: String) {
        val newMessage = ChatMessage(
            chatMessageId = fetchedMessages.size + 1,
            content = content,
            dateTime = getCurrentDateTime(),
            senderId = viewModel.chatParticipants.value!!,
            groupChatId = null,
            chatId = chatId,
            groupChat = null,
            chat = null
        )
        val otherUserId = viewModel.chats.value!!.filter { it.chatId == chatId }[0].otherUserId
        viewModel.sendMessage(otherUserId, content) {
            if(it) {
                fetchedMessages.add(newMessage)
                adapter.notifyItemInserted(fetchedMessages.size - 1)
                recyclerView.scrollToPosition(fetchedMessages.size - 1)
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}