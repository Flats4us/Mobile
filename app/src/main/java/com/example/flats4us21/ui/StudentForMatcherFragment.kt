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
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentStudentForMatcherBinding
import com.example.flats4us21.viewmodels.MatcherViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.Locale

private const val TAG = "StudentForMatcherFragment"

class StudentForMatcherFragment : Fragment() {
    private var _binding: FragmentStudentForMatcherBinding? = null
    private val binding get() = _binding!!
    private val matcherViewModel: MatcherViewModel by activityViewModels()

    companion object {
        private const val ARG_STUDENT = "student"

        fun newInstance(student: StudentForMatcher): StudentForMatcherFragment {
            return StudentForMatcherFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_STUDENT, student)
                }
            }
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
        arguments?.getParcelable<StudentForMatcher>(ARG_STUDENT)?.let { student ->
            bindData(student)
            setupButtons(student)
        }
    }

    private fun bindData(student: StudentForMatcher) {
        with(binding) {
            profileName.text = student.name
            profileAge.text = getString(R.string.age_with_value, student.age.toString())
            profileUniversity.text = getString(R.string.univerity_with_value, student.university)

            val url = "$URL/${student.profilePicture?.path.orEmpty()}"
            Log.i(TAG, url)
            profileImage.load(url) {
                error(R.drawable.baseline_person_24)
            }

            if (student.interest.isNullOrEmpty()) {
                interestsRecyclerView.visibility = View.GONE
            } else {
                val interests = student.interest.map { QuestionTranslator.translateInterestName(it.interestName.lowercase(
                    Locale.getDefault()), requireContext()) }
                interestsRecyclerView.apply {
                    visibility = View.VISIBLE
                    adapter = InterestAdapter(interests)
                    layoutManager = FlexboxLayoutManager(context).apply {
                        flexDirection = FlexDirection.ROW
                        justifyContent = JustifyContent.FLEX_START
                    }
                }
            }
        }
    }

    private fun setupButtons(student: StudentForMatcher) {
        binding.acceptButton.setOnClickListener {
            matcherViewModel.addMatchDecision(student.userId, true)
        }

        binding.rejectButton.setOnClickListener {
            matcherViewModel.addMatchDecision(student.userId, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
