package com.example.flats4us21.services

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flats4us21.databinding.FragmentArgumentsBinding
import com.example.flats4us21.viewmodels.OfferViewModel

private const val TAG = "ArgumentsFragment"
class ArgumentsFragment : Fragment() {
    private var _binding : FragmentArgumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArgumentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading && viewModel.pageNumber == 1) View.VISIBLE else View.GONE
            binding.chatsRecyclerView.visibility = if (isLoading && viewModel.pageNumber == 1) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}