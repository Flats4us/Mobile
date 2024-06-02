package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.InterestAdapter
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.databinding.FragmentStudentForMatcherBinding
import com.example.flats4us21.viewmodels.MatcherViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

private const val TAG = "StudentForMatcherFragment"
class StudentForMatcherFragment : Fragment() {
    private var _binding: FragmentStudentForMatcherBinding? = null
    private val binding get() = _binding!!
    private val matcherViewModel: MatcherViewModel by activityViewModels()

    companion object {
        private const val ARG_STUDENT = "student"

        fun newInstance(student: StudentForMatcher): StudentForMatcherFragment {
            val fragment = StudentForMatcherFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_STUDENT, student)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentForMatcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val student = arguments?.getParcelable<StudentForMatcher>(ARG_STUDENT)
        student?.let {
            binding.profileName.text = it.name
            binding.profileAge.text = "Age: ${it.age}"
            binding.profileUniversity.text = "University: ${it.university}"

            val url = "$URL/${it.profilePicture?.path ?: ""}"
            Log.i(TAG, url)
            binding.profileImage.load(url) {
                error(R.drawable.baseline_person_24)
            }

            if(it.interest.isNullOrEmpty()){
                binding.interestsRecyclerView.visibility = View.GONE
            } else {
                val recyclerView = binding.interestsRecyclerView
                binding.interestsRecyclerView.visibility = View.VISIBLE
                val adapter = InterestAdapter(it.interest)
                recyclerView.adapter = adapter
                val flexboxLayoutManager = FlexboxLayoutManager(context)
                flexboxLayoutManager.flexDirection = FlexDirection.ROW
                flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
                recyclerView.layoutManager = flexboxLayoutManager
            }

            binding.acceptButton.setOnClickListener { _ ->
                matcherViewModel.addMatchDecision(it.userId, true)
            }

            binding.rejectButton.setOnClickListener { _ ->
                matcherViewModel.addMatchDecision(it.userId, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
