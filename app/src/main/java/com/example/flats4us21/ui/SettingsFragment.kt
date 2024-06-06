package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationLayout.setOnClickListener {
            val fragment = SettingsNotificationFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.changePasswordLayout.setOnClickListener {
            val fragment = SettingsChangePasswordFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.changeEmailLayout.setOnClickListener {
            val fragment = SettingsChangeEmailFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}