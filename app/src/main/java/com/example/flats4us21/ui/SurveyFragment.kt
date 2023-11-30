package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.QuestionAdapter
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.databinding.FragmentSurveyBinding
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "SurveyFragment"
class SurveyFragment : Fragment() {
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private var _binding: FragmentSurveyBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private val fetchedQuestions: MutableList<SurveyQuestion> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(fetchedQuestions.size == 0)
            userViewModel.getQuestionList(userViewModel.userType!!)
        questionRecyclerView = binding.questionRecyclerView

        userViewModel.questionList.observe(viewLifecycleOwner) { questions ->
            Log.i(TAG, "Number of questions: ${questions.size}")
            fetchedQuestions.addAll(questions)
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            questionRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        questionRecyclerView = binding.questionRecyclerView
        questionAdapter = QuestionAdapter(fetchedQuestions)
        questionRecyclerView.adapter = questionAdapter
        questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val prevButton = binding.prevButton
        prevButton.setOnClickListener {
            collectData()
            val fragment = RegisterSpecificDataFragment()
            (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
            (requireParentFragment() as RegisterParentFragment).decreaseProgressBar()
        }

        val nextButton = binding.nextButton
        nextButton.setOnClickListener {
            if(validateData()){
                collectData()
                val fragment = RegisterAddDocumentFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun validateData(): Boolean {
        //TODO: Not yet implemented
        return true
    }

    private fun collectData() {
        val answers :  List<QuestionResponse> = questionAdapter.getAllAnswers()
        Log.i(TAG, "[collectData] Answers: $answers")
        userViewModel.questionResponseList = answers
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
