package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us.R
import com.example.flats4us.databinding.FragmentRoommatesBinding
import com.google.android.material.tabs.TabLayout


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

        binding.buttons.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(StudentMatchesFragment())
                    1 -> replaceFragment(StudentExistingMatchesFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

    }

    fun replaceFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.matches, fragment)
            .addToBackStack(null)
            .commit()
    }


}