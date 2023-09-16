package com.example.flats4us21

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.QuestionAdapter
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.databinding.ActivityOwnerSurveyBinding
import com.example.flats4us21.services.StudentSurveyService
import com.example.flats4us21.viewmodels.MainViewModel

class OwnerSurveyActivity : AppCompatActivity() {
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter : QuestionAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var binding : ActivityOwnerSurveyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //Setting list of questions in MainViewModel
        val studentSurveyService = StudentSurveyService(viewModel)
        studentSurveyService.getSurveyQuestion()
        val listOfQuestions = viewModel.getQuestionList()
        listOfQuestions.observe(this) { questions ->
            questionRecyclerView = binding.questionRecyclerView
            questionAdapter = QuestionAdapter(questions)
            questionRecyclerView.adapter = questionAdapter
            questionRecyclerView.layoutManager = LinearLayoutManager(this)
        }

        val sendButton = binding.sendButton
        sendButton.setOnClickListener {
            val answers :  List<QuestionResponse> = questionAdapter.getAllAnswers()
            studentSurveyService.postSurveyQuestions(answers)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("Menu", "onCreateOptionsMenu() called")
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Menu", "onOptionsItemSelected() called, item id: ${item.itemId}")
        when (item.itemId){
            R.id.about -> Toast.makeText(this,"About Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this,"Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.exit -> Toast.makeText(this,"Exit Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}