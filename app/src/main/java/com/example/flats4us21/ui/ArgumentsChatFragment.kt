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
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ArgumentMessageAdapter
import com.example.flats4us21.data.Argument
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.data.GroupChatInfo
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentArgumentsChatBinding
import com.example.flats4us21.viewmodels.ArgumentViewModel
import com.example.flats4us21.viewmodels.ChatViewModel
import com.example.flats4us21.viewmodels.UserViewModel
import java.util.Locale

private const val TAG = "ArgumentsChatFragment"
class ArgumentsChatFragment : Fragment() {
    private var _binding : FragmentArgumentsChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var argumentViewModel: ArgumentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArgumentMessageAdapter
    private var fetchedMessages: MutableList<ChatMessage> = mutableListOf()
    private var chatInfo: GroupChatInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        argumentViewModel = ViewModelProvider(requireActivity())[ArgumentViewModel::class.java]
        _binding = FragmentArgumentsChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startConnection()

        val chatId = arguments?.getInt(CHAT_ID)
        val chatName = arguments?.getString(CHAT_NAME)

        if(chatId != null) {
            viewModel.getGroupChatInfo(chatId) {
                if (it)
                    viewModel.getGroupChatHistory(chatId)
            }
        }
        if(chatName != null) {
            bindChatInfoData(chatName)
        }
        argumentViewModel.getArgument()

        argumentViewModel.userArguments.observe(viewLifecycleOwner) {
            val argument = argumentViewModel.userArguments.value!!.first { argument -> argument.groupChatId == chatId }

            bindArgumentData(argument)
        }


        recyclerView = binding.chatRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d(TAG, "Fetched User id: ${userViewModel.myProfile.value!!.userId}")
        adapter = ArgumentMessageAdapter(fetchedMessages, userViewModel.myProfile.value!!.userId, chatInfo?.users)
        recyclerView.adapter = adapter

        viewModel.groupChatInfo.observe(viewLifecycleOwner) { chatInfo ->
            Log.d(TAG, "Observed chat info: $chatInfo")
            adapter.updateChatUsers(chatInfo.users)
        }

        viewModel.chatHistory.observe(viewLifecycleOwner) { chatHistory ->
            Log.d(TAG, "Observed chat history: $chatHistory")
            if (chatId != null) {
                bindMessageData(chatHistory as MutableList<ChatMessage>)
            }
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

        binding.closeAcceptanceInformationButton.setOnClickListener {
            binding.acceptanceInformationLayout.visibility = View.GONE
        }

        binding.infoButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(CHAT_ID, chatId!!)
            val fragment = ArgumentChatSettingFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                addMessage(messageContent)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun bindArgumentData(argument: Argument) {
        if(argument.ownerAcceptanceDate.isNullOrEmpty() && !argument.studentAccceptanceDate.isNullOrEmpty() && DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT) == UserType.OWNER.toString() ||
            !argument.ownerAcceptanceDate.isNullOrEmpty() && argument.studentAccceptanceDate.isNullOrEmpty() && DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT) == UserType.STUDENT.toString()) {
            binding.acceptanceInformationLayout.visibility = View.VISIBLE
        } else {
            binding.acceptanceInformationLayout.visibility = View.GONE
        }

        if(!argument.ownerAcceptanceDate.isNullOrEmpty() && !argument.studentAccceptanceDate.isNullOrEmpty()) {
            binding.chatStatus.visibility = View.VISIBLE
            binding.message.visibility = View.GONE
        } else {
            binding.chatStatus.visibility = View.GONE
            binding.message.visibility = View.VISIBLE
        }
    }

    private fun bindChatInfoData(name: String) {
        if (chatInfo != null) {
            binding.groupName.text = name
        }
    }

    private fun bindMessageData(chatHistory: MutableList<ChatMessage>) {
        fetchedMessages.clear()
        fetchedMessages.addAll(chatHistory)
        Log.d(TAG, "Fetched messages after update: $fetchedMessages")

        adapter.updateMessages(fetchedMessages)
        recyclerView.scrollToPosition(fetchedMessages.size - 1)
    }

    private fun addMessage(content: String) {
        val userChats = viewModel.groupChatInfo.value
        if (userChats != null) {
            val groupChatId = viewModel.groupChatInfo.value!!.groupChatId
            viewModel.sendGroupMessage(groupChatId, content, userViewModel.myProfile.value!!.userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.stopConnection()
        _binding = null
    }

}
