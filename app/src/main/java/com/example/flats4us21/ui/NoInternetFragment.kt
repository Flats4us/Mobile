package com.example.flats4us21.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentNoInternetBinding

class NoInternetFragment : Fragment() {
    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retryButton.setOnClickListener {
            if (isNetworkConnected()) {
                (activity as? DrawerActivity)?.goBack()
            } else {
                Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}