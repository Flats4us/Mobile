package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.databinding.FragmentArgumentsChatBinding
import com.example.flats4us21.viewmodels.OfferViewModel


class ArgumentsChatFragment : Fragment() {
    private var _binding : FragmentArgumentsChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArgumentsChatBinding.inflate(inflater, container, false)
        return binding.root
    }


}