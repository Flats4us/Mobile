package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentAddRealEstateBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateFragment : Fragment() {
    private var _binding : FragmentAddRealEstateBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstateViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        _binding = FragmentAddRealEstateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(AddRealEstateFirstStepFragment())
        increaseProgressBar()
    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.addRealEstateStep, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun increaseProgressBar(){
        binding.progressBar.incrementProgressBy(25)
    }

    fun decreaseProgressBar(){
        binding.progressBar.incrementProgressBy(-25)
    }

    fun decreaseProgressBar(diff: Int){
        binding.progressBar.incrementProgressBy(diff)
    }

}