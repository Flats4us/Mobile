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
import com.example.flats4us21.data.Argument
import com.example.flats4us21.databinding.FragmentArgumentsBinding
import com.example.flats4us21.viewmodels.ArgumentViewModel
import com.google.android.material.tabs.TabLayout

private const val TAG = "ArgumentsFragment"
class ArgumentsFragment : Fragment() {
    private var _binding : FragmentArgumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArgumentAdapter
    private lateinit var viewModel : ArgumentViewModel
    private var fetchedArguments: MutableList<Argument> = mutableListOf()

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

        binding.buttonsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> updateArgumentList(false)
                    1 -> updateArgumentList(true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        setupRecyclerView()
        setupObservers()

        viewModel.getArgument()


    }

    private fun updateArgumentList(isFinished: Boolean) {
        val filteredArguments = if (isFinished) {
            fetchedArguments.filter { it.argumentStatus == 1 }
        } else {
            fetchedArguments.filter { it.argumentStatus == 0 }
        }
        Log.i(TAG, "Filtered arguments: $filteredArguments")
        adapter.updateArguments(filteredArguments)

        if (filteredArguments.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.chatsRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.chatsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        adapter = ArgumentAdapter(fetchedArguments) { selectedChat ->
            val bundle = Bundle()
            bundle.putInt(CHAT_ID, selectedChat.groupChatId)
            val fragment = ArgumentsChatFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        
        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatsRecyclerView.adapter = adapter
        
    }

    private fun setupObservers() {
        viewModel.userArguments.observe(viewLifecycleOwner) { arguments ->
            fetchedArguments = arguments.toMutableList()
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