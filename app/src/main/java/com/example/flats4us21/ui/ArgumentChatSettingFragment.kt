package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.data.Argument
import com.example.flats4us21.data.UserType
import com.example.flats4us21.databinding.FragmentArgumentChatSettingBinding
import com.example.flats4us21.viewmodels.ArgumentViewModel
import com.example.flats4us21.viewmodels.ChatViewModel
import java.util.Locale


class ArgumentChatSettingFragment : Fragment() {
    private var _binding : FragmentArgumentChatSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var argumentViewModel: ArgumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        argumentViewModel = ViewModelProvider(requireActivity())[ArgumentViewModel::class.java]
        _binding = FragmentArgumentChatSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val groupChatId = arguments?.getInt(CHAT_ID)
        argumentViewModel.getArgument()

        argumentViewModel.userArguments.observe(viewLifecycleOwner) {
            val argument = argumentViewModel.userArguments.value!!.first { argument -> argument.groupChatId == groupChatId }

            bindData(argument)
            setListeners(argument)
        }

        argumentViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                argumentViewModel.clearErrorMessage()
            }
        }

        argumentViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.mainLayout.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    private fun setListeners(argument: Argument) {
        val argumentId = argument.argumentId
        binding.usersLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(CHAT_ID, argument.groupChatId)
            val fragment = ArgumentChatUsersFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.acceptLayout.setOnClickListener {
            Log.d("DataStoreManager", "DataStoreManager.userRole.toString().uppercase(Locale.ROOT) ${DataStoreManager.userRole.toString().uppercase(Locale.ROOT)} ==  UserType.STUDENT.toString() ${UserType.STUDENT.toString()} ${DataStoreManager.userRole.toString().uppercase(Locale.ROOT) == UserType.STUDENT.toString()}")
            if(DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT) == UserType.STUDENT.toString()) {
                argumentViewModel.studentAcceptArgument(argumentId) {
                    if(it) {
                        (activity as? DrawerActivity)!!.goBack()
                    }
                }
            } else {
                argumentViewModel.ownerAcceptArgument(argumentId) {
                    if(it) {
                        (activity as? DrawerActivity)!!.goBack()
                    }
                }
            }
        }
        binding.askForHelpLayout.setOnClickListener {
           argumentViewModel.askingForIntervention(argumentId) {
               if(it) {
                   (activity as? DrawerActivity)!!.goBack()
               }
           }
        }
    }

    private fun bindData(argument: Argument) {
        binding.title.text = viewModel.groupChatInfo.value!!.name
        val acceptVisibility = !argument.studentAccceptanceDate.isNullOrEmpty()
                && DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT) == UserType.STUDENT.toString()
                || !argument.ownerAcceptanceDate.isNullOrEmpty()
                && DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT) == UserType.OWNER.toString()
        Log.d("acceptVisibility", " argument.ownerAcceptanceDate.isNullOrEmpty() ${argument.ownerAcceptanceDate.isNullOrEmpty()} ${DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT)} == ${UserType.STUDENT.toString()} $acceptVisibility")
        Log.d("acceptVisibility", " ${argument.studentAccceptanceDate.isNullOrEmpty()} ${DataStoreManager.userRole.value.toString().uppercase(Locale.ROOT)} == ${UserType.OWNER.toString()} $acceptVisibility")

        if(acceptVisibility) {
            binding.acceptLayout.visibility = View.GONE
        } else {
            binding.acceptLayout.visibility = View.VISIBLE
        }

        if(argument.interventionNeed) {
            binding.askForHelpLayout.visibility = View.GONE
        } else {
            binding.askForHelpLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}