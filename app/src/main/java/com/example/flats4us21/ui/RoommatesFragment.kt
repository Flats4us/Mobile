package com.example.flats4us21.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentRoommatesBinding


class RoommatesFragment : Fragment() {
    private var _binding : FragmentRoommatesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoommatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replaceFragment(StudentMatchesFragment())

        binding.potentialButton.setOnClickListener {
            binding.potentialButton.typeface = Typeface.DEFAULT_BOLD
            binding.existingButton.typeface = Typeface.DEFAULT
            replaceFragment(StudentMatchesFragment())
        }

        binding.existingButton.setOnClickListener {
            binding.existingButton.typeface = Typeface.DEFAULT_BOLD
            binding.potentialButton.typeface = Typeface.DEFAULT
            replaceFragment(StudentExistingMatchesFragment())
        }
    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.matches, fragment)
            .addToBackStack(null)
            .commit()
    }


}