package com.example.flats4us21.adapters

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.databinding.AnswerTextRowBinding
import com.example.flats4us21.databinding.RadiobuttonRowBinding
import com.example.flats4us21.databinding.SubQuestionRowBinding
import com.google.gson.Gson

class AnswerAdapter(
    private val type : String,
    private val answers: List<Any?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedAnswerPosition: Int = RecyclerView.NO_POSITION

    inner class RadioButtonViewHolder(binding: RadiobuttonRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val radiobutton = binding.radioButton

        init{
            radiobutton.setOnClickListener {
                selectedAnswerPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    inner class TextViewHolder(binding: AnswerTextRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val answerText = binding.textAnswer
    }

    inner class SubquestionViewHolder(binding: SubQuestionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val sub_question = binding.subquestionText
        val sub_answers = binding.subquestionRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            "RADIOBUTTON" -> {
                val binding = RadiobuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RadioButtonViewHolder(binding)
            }
            "TEXT" -> {
                val binding = AnswerTextRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TextViewHolder(binding)
            }
            "SUB-QUESTION" -> {
                val binding = SubQuestionRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SubquestionViewHolder(binding)
            }
            else -> {
                val binding = AnswerTextRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TextViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val answer = answers[position]
        when (holder) {
            is TextViewHolder -> {
                holder.answerText.inputType = when(answer){
                    "Text" -> InputType.TYPE_CLASS_TEXT
                    else -> {InputType.TYPE_CLASS_TEXT}
                }
            }
            is RadioButtonViewHolder -> {
                holder.radiobutton.text = answer as String
                holder.radiobutton.isChecked = selectedAnswerPosition == position
            }
            is SubquestionViewHolder -> {
                val question = answer as SurveyQuestion
                holder.sub_question.text = question.content
                holder.sub_answers.adapter = AnswerAdapter(question.responseType ,question.answers)
                holder.sub_answers.layoutManager  = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun getSelectedAnswer(): String? {
        if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
            return answers[selectedAnswerPosition] as String
        }
        return null
    }

}
