package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentAddRealEstateBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel

class AddRealEstateFragment : Fragment() {
    private var _binding : FragmentAddRealEstateBinding? = null
    private val binding get() = _binding!!
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var activeStep : TextView

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
        activeStep = binding.firstStep
        replaceFragment(AddRealEstateFirstStepFragment())
    }

    private fun setActiveStep(textView: TextView){
        activeStep.setBackgroundResource(R.drawable.background_header)
        activeStep = textView
        activeStep.setBackgroundResource(R.drawable.background_active_step)
    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.addRealEstateStep, fragment)
            .addToBackStack(null)
            .commit()
        if ((fragment is AddRealEstateFirstStepFragment)){
            setActiveStep(binding.firstStep)
        }else if(fragment is AddRealEstateSecondStepFragment){
            setActiveStep(binding.secondStep)
        }else if(fragment is AddRealEstateThirdStepFragment){
            setActiveStep(binding.thirdStep)
        }else{
            setActiveStep(binding.fourthStep)
        }
    }

}