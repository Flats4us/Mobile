package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.ExistingMatchesAdapter
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.databinding.FragmentStudentExistingMatchesBinding
import com.example.flats4us21.viewmodels.MatcherViewModel


class StudentExistingMatchesFragment : Fragment() {
    private var _binding: FragmentStudentExistingMatchesBinding? = null
    private val binding get() = _binding!!
    private val matcherViewModel: MatcherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentExistingMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matcherViewModel.getExistingMatches()

        matcherViewModel.existingMatches.observe(viewLifecycleOwner) { matches ->
            bindData(matches)
        }

        matcherViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.matchesRecyclerView.visibility = if(isLoading) View.GONE else View.VISIBLE
        }

        matcherViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun bindData(matches: MutableList<StudentForMatcher>){
        val adapter = ExistingMatchesAdapter(
            matches
        )
        { position ->
            val userId = matches[position].userId
            val bundle = Bundle().apply {
                putInt(USER_ID, userId)
            }
            //TODO: replace with actual fragment
            val fragment = ProfileFragment().apply {
                arguments = bundle
            }
            (activity as? DrawerActivity)?.replaceFragment(fragment)
        }

        if(matches.isEmpty()){
            binding.noDataText.visibility = View.VISIBLE
            binding.matchesRecyclerView.visibility = View.GONE
        } else {
            binding.noDataText.visibility = View.GONE
            binding.matchesRecyclerView.visibility = View.VISIBLE
        }

        binding.matchesRecyclerView.adapter = adapter
        binding.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}