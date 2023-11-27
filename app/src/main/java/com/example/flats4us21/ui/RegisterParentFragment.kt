package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentRegisterParentBinding
import com.example.flats4us21.viewmodels.UserViewModel

class RegisterParentFragment : Fragment() {
    private var _binding : FragmentRegisterParentBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentRegisterParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(RegisterSelectingUserTypeFragment())

    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.registerStep, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun increaseProgressBar(){
        val incrementValue = if(userViewModel.userType.toString() == "STUDENT") 25 else 20
        binding.progressBar.incrementProgressBy(incrementValue)
    }

    fun decreaseProgressBar(){
        val incrementValue = if(userViewModel.userType.toString() == "STUDENT") -25 else -20
        binding.progressBar.incrementProgressBy(incrementValue)
    }

    fun decreaseProgressBar(diff: Int){
        binding.progressBar.incrementProgressBy(diff)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}