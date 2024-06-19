package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ExistingMatchesAdapter
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.databinding.FragmentStudentExistingMatchesBinding
import com.example.flats4us21.viewmodels.ChatViewModel
import com.example.flats4us21.viewmodels.MatcherViewModel

class StudentExistingMatchesFragment : Fragment() {
    private var _binding: FragmentStudentExistingMatchesBinding? = null
    private val binding get() = _binding!!
    private val matcherViewModel: MatcherViewModel by activityViewModels()
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        _binding = FragmentStudentExistingMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        matcherViewModel.getExistingMatches()
    }

    private fun setupObservers() {
        matcherViewModel.existingMatches.observe(viewLifecycleOwner) { matches ->
            bindData(matches)
        }

        matcherViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.matchesRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        matcherViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bindData(matches: MutableList<StudentForMatcher>) {
        val adapter = ExistingMatchesAdapter(matches) { position ->
            navigateToChat(matches[position].userId)
        }

        if (matches.isEmpty()) {
            binding.noDataText.visibility = View.VISIBLE
            binding.matchesRecyclerView.visibility = View.GONE
        } else {
            binding.noDataText.visibility = View.GONE
            binding.matchesRecyclerView.visibility = View.VISIBLE
            binding.matchesRecyclerView.adapter = adapter
            binding.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun navigateToChat(userId: Int) {
        checkIfChatAlreadyExists(userId) { chatId, exists ->
            val bundle = Bundle()
            bundle.putInt(USER_ID, userId)
            val fragment = ChatFragment()
            if(exists) {
                Log.d("ChatFragment", "Chat already exists")
                bundle.putInt(CHAT_ID, chatId)
            }
            bundle.putBoolean(IS_CREATING, !exists)
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
        }
    }

    private fun checkIfChatAlreadyExists(userId: Int, callback: (Int, Boolean) -> Unit) {
        chatViewModel.getUserChats()
        chatViewModel.userChats.observe(viewLifecycleOwner) { chats ->
            val chat = chats.firstOrNull { it.otherUserId == userId }
            if (chat != null) {
                callback(chat.chatId, true)
            } else {
                callback(0, false)
            }
            chatViewModel.userChats.removeObservers(viewLifecycleOwner)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
