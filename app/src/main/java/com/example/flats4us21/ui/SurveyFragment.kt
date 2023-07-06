package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.QuestionAdapter
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.databinding.FragmentSurveyBinding
import com.example.flats4us21.services.StudentSurveyService
import com.example.flats4us21.viewmodels.MainViewModel

class SurveyFragment : Fragment() {
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private var _binding: FragmentSurveyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // Setting list of questions in MainViewModel
        val studentSurveyService = StudentSurveyService(viewModel)
        studentSurveyService.getSurveyQuestion()
        val listOfQuestions = viewModel.getQuestionList()
        listOfQuestions.observe(viewLifecycleOwner) { questions ->
            questionRecyclerView = binding.questionRecyclerView
            questionAdapter = QuestionAdapter(questions)
            questionRecyclerView.adapter = questionAdapter
            questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        val sendButton = binding.sendButton
        sendButton.setOnClickListener {
            val answers: List<QuestionResponse> = questionAdapter.getAllAnswers()
            studentSurveyService.postSurveyQuestions(answers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
