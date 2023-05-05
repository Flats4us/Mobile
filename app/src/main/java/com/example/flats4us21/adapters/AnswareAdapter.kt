package com.example.flats4us21.adapters

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.databinding.AnswerTextRowBinding
import com.example.flats4us21.databinding.CheckboxRowBinding

class AnswerAdapter(
    private val type : String,
    private val answers: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedAnswerPosition: Int = RecyclerView.NO_POSITION

    inner class CheckboxViewHolder(binding: CheckboxRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val radiobutton = binding.radioButton
    }

    inner class TextViewHolder(binding: AnswerTextRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val answerText = binding.textAnswer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            "CHECKBOX" -> {
                val binding = CheckboxRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CheckboxViewHolder(binding)
            }
            "TEXT" -> {
                val binding = AnswerTextRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TextViewHolder(binding)
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
                    "Integer" -> InputType.TYPE_CLASS_NUMBER
                    "Text" -> InputType.TYPE_CLASS_TEXT
                    else -> {InputType.TYPE_CLASS_TEXT}
                }
            }
            is CheckboxViewHolder -> {
                holder.radiobutton.text = answer
                holder.radiobutton.isChecked = selectedAnswerPosition == position
                holder.itemView.setOnClickListener {
                    selectedAnswerPosition = position
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun getSelectedAnswer(): String? {
        if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
            return answers[selectedAnswerPosition]
        }
        return null
    }

}
