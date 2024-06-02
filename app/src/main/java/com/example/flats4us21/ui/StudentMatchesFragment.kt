package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.adapters.MatchAdapter
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.databinding.FragmentStudentMatchesBinding
import com.example.flats4us21.viewmodels.MatcherViewModel


class StudentMatchesFragment : Fragment() {
    private var _binding: FragmentStudentMatchesBinding? = null
    private val binding get() = _binding!!
    private val matcherViewModel: MatcherViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: MatchAdapter
    private var fetchedMatches: MutableList<StudentForMatcher> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matcherViewModel.getPotentialMatches()

        viewPager = binding.viewPager
        adapter = MatchAdapter(requireActivity(), fetchedMatches)
        viewPager.adapter = adapter


        matcherViewModel.potentialMatches.observe(viewLifecycleOwner) { students ->
            fetchedMatches = students
            adapter.updateList(fetchedMatches)
            if(fetchedMatches.isEmpty()) {
                viewPager.visibility = View.GONE
                binding.noDataText.visibility = View.VISIBLE
            } else {
                viewPager.visibility = View.VISIBLE
                binding.noDataText.visibility = View.GONE
            }

        }

        matcherViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            viewPager.visibility = if(isLoading) View.GONE else View.VISIBLE
        }

        matcherViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

    }
}