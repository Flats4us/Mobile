package com.example.flats4us.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us.DrawerActivity
import com.example.flats4us.databinding.FragmentStartScreenBinding


class StartScreenFragment : Fragment() {
    private var _binding : FragmentStartScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            val fragment = LoginFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
        binding.buttonRegister.setOnClickListener {
            val fragment = RegisterParentFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}