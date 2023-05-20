package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.databinding.QuestionRowBinding

class QuestionAdapter(
    private val questions : List<SurveyQuestion>
    ) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {

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
        holder.recyclerView.adapter = AnswerAdapter(questions[position].responseType ,questions[position].answers)
        val layoutManager : LinearLayoutManager = if(questions[position].responseType == "SUB-QUESTION"){
            LinearLayoutManager(holder.itemView.context)
        } else{
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
        holder.recyclerView.layoutManager = layoutManager
    }
}