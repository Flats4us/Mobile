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
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ArgumentAdapter
import com.example.flats4us21.databinding.FragmentArgumentsBinding
import com.example.flats4us21.viewmodels.ArgumentViewModel

private const val TAG = "ArgumentsFragment"
class ArgumentsFragment : Fragment() {
    private var _binding : FragmentArgumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArgumentAdapter
    private lateinit var viewModel : ArgumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ArgumentViewModel::class.java]
        _binding = FragmentArgumentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.getArgument()


    }

    private fun setupRecyclerView() {
        adapter = ArgumentAdapter(mutableListOf()) { selectedChat ->
            val bundle = Bundle()
            bundle.putInt(CHAT_ID, selectedChat.groupChatId)
            val fragment = ArgumentsChatFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ArgumentsFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.userArguments.observe(viewLifecycleOwner) { arguments ->
            adapter.updateArguments(arguments)
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.chatsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}