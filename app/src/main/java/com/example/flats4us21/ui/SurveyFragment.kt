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
    private  var userResponses: Map<String, Any>? = null

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

        userViewModel.getQuestionList(userViewModel.userType!!)

        userResponses = userViewModel.userResponses

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
        questionAdapter = QuestionAdapter(fetchedQuestions, userResponses)
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
                val fragment = RegisterLogInDataFragment()
                (requireParentFragment() as RegisterParentFragment).replaceFragment(fragment)
                (requireParentFragment() as RegisterParentFragment).increaseProgressBar()
            }
        }
    }

    private fun validateData(): Boolean {
        userResponses = questionAdapter.getUserResponses()
        if(userResponses != null) {
            if(
                userResponses!!["party"] == null ||
                userResponses!!["tidiness"] == null ||
                userResponses!!["smoking"] == null ||
                userResponses!!["sociability"] == null ||
                userResponses!!["animals"] == null ||
                userResponses!!["vegan"] == null ||
                userResponses!!["lookingForRoommate"] == null ||
                userResponses!!["lookingForRoommate"] == true &&
                (userResponses!!["maxNumberOfRoommates"] == null ||
                        userResponses!!["roommateGender"] == null ||
                        userResponses!!["minRoommateAge"] == null ||
                        userResponses!!["maxRoommateAge"] == null ||
                        userResponses!!["city"] == null)
            ) {
                Toast.makeText(requireContext(), "Please answer all required questions.", Toast.LENGTH_LONG).show()
                return false
            } else {
                return true
            }
        } else {
            return false
        }
    }

    private fun collectData() {
        userResponses = questionAdapter.getUserResponses()

        if(userResponses != null) {
            userViewModel.party = userResponses!!["party"] as? Int ?: 0
            userViewModel.tidiness = userResponses!!["tidiness"] as? Int ?: 0
            userViewModel.smoking = userResponses!!["smoking"] as? Boolean ?: false
            userViewModel.sociability = userResponses!!["sociability"] as? Boolean ?: false
            userViewModel.animals = userResponses!!["animals"] as? Boolean ?: false
            userViewModel.vegan = userResponses!!["vegan"] as? Boolean ?: false
            userViewModel.lookingForRoommate = userResponses!!["lookingForRoommate"] as? Boolean ?: false
            userViewModel.maxNumberOfRoommates = userResponses!!["maxNumberOfRoommates"] as? Int ?: 0
            userViewModel.roommateGender = userResponses!!["roommateGender"] as? Int ?: 0
            userViewModel.minRoommateAge = userResponses!!["minRoommateAge"] as? Int ?: 0
            userViewModel.maxRoommateAge = userResponses!!["maxRoommateAge"] as? Int ?: 0
            userViewModel.city = userResponses!!["city"] as? String ?: ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


