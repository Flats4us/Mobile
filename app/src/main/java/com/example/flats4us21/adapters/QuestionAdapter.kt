package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.ResponseType
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.databinding.QuestionRowBinding

class QuestionAdapter(
    private val questions : List<SurveyQuestion>
    ) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {

    private val selectedAnswers: MutableMap<Int, AnswerAdapter> = mutableMapOf()

    inner class MyViewHolder(binding : QuestionRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.content
        val recyclerView = binding.answerRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = QuestionRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.content.text = questions[position].content
        val adapter = AnswerAdapter(questions[position].responseType ,questions[position].answers)
        selectedAnswers[questions[position].questionId] = adapter
        holder.recyclerView.adapter = adapter
        val layoutManager : LinearLayoutManager = when (questions[position].responseType) {
            ResponseType.SUBQUESTION -> {
                LinearLayoutManager(holder.itemView.context)
            }
            ResponseType.SLIDER -> {
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            }
            ResponseType.SWITCH -> {
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            }
            else -> {
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        holder.recyclerView.layoutManager = layoutManager
    }

    fun getAllAnswers() : List<QuestionResponse>{
        val answers = mutableListOf<QuestionResponse>()
        for (question in selectedAnswers){
            if(question.value.get() == ResponseType.SUBQUESTION){
                answers.addAll(question.value.getSubanswers())
            } else {
                val questionId = question.key
                val questionAnswer = question.value.getAnswer()
                answers.add(QuestionResponse(questionId, questionAnswer!!))
            }
        }
        return answers
    }
}