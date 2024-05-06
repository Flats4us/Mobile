package com.example.flats4us21.adapters

import android.annotation.SuppressLint
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.ResponseType
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.databinding.*

private const val TAG = "AnswerAdapter"
class AnswerAdapter(
    private val type : ResponseType,
    private val answers: List<Any?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedAnswerPosition: Int = RecyclerView.NO_POSITION
    private lateinit var answerHolder: RecyclerView.ViewHolder
    private var subAdapters: MutableMap<Int, AnswerAdapter> = mutableMapOf()

    @SuppressLint("NotifyDataSetChanged")
    inner class RadioButtonViewHolder(binding: RadiobuttonRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val radiobutton = binding.radioButton

        init{
            radiobutton.setOnClickListener {
                selectedAnswerPosition = bindingAdapterPosition
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

    inner class SliderViewHolder(binding: AnswerSliderRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val seekBar = binding.seekBar
        }
    inner class SwitchViewHolder(binding: AnswerSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val switch = binding.switchCompat
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            ResponseType.RADIOBUTTON -> {
                val binding = RadiobuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RadioButtonViewHolder(binding)
            }
            ResponseType.TEXT -> {
                val binding = AnswerTextRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TextViewHolder(binding)
            }
            ResponseType.SUBQUESTION -> {
                val binding = SubQuestionRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SubquestionViewHolder(binding)
            }
            ResponseType.SLIDER -> {
                val binding = AnswerSliderRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SliderViewHolder(binding)
            }
            ResponseType.SWITCH -> {
                val binding = AnswerSwitchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Log.d(TAG, "TextView visibility: ${binding.switchCompat.visibility}")
                SwitchViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val answer = answers[position]
        answerHolder = holder
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
                val adapter = AnswerAdapter(question.responseType ,question.answers)
                subAdapters[question.questionId] = adapter
                holder.sub_answers.adapter = adapter
                holder.sub_answers.layoutManager  = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
            is SliderViewHolder -> {
                val pair : Pair<Int, Int> = (answer as Pair<Int, Int>)
                holder.seekBar.valueFrom = pair.first.toFloat()
                holder.seekBar.value = pair.first.toFloat()
                holder.seekBar.valueTo = pair.second.toFloat()
            }
            is SwitchViewHolder -> {
                holder.switch.isChecked = answer as Boolean
            }
        }
    }

    fun getSelectedAnswer(): String? {
        if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
            return answers[selectedAnswerPosition] as String
        }
        return null
    }

    fun getAnswer(): String {
        return when (type) {
            ResponseType.RADIOBUTTON -> ({
                if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
                    answers[selectedAnswerPosition]
                } else {
                    "null"
                }
            }).toString()
            ResponseType.TEXT -> {
                val textHolder = answerHolder as TextViewHolder
                return textHolder.answerText.text.toString()
            }
            ResponseType.SUBQUESTION -> {
                "null"
            }
            ResponseType.SLIDER -> {
                val sliderHolder = answerHolder as SliderViewHolder
                return sliderHolder.seekBar.value.toInt().toString()
            }
            ResponseType.SWITCH -> {
                val switchHolder = answerHolder as SwitchViewHolder
                return switchHolder.switch.isChecked.toString()
            }
        }
    }

    fun getSubanswers() : List<QuestionResponse>{
        val responses = mutableListOf<QuestionResponse>()
        for (question in subAdapters){
            val questionId = question.key
            val questionAnswer = question.value.getSelectedAnswer()
            responses.add(QuestionResponse(questionId, questionAnswer!!))
        }
        return responses
    }


    fun get(): ResponseType {
        return type
    }

//    fun getAllAnswers() : List<QuestionResponse>{
//        val answers = mutableListOf<QuestionResponse>();
//        for (question in selectedAnswers){
//
//            val questionId = question.key
//            val questionAnswer = question.value.getSelectedAnswer()
//            answers.add(QuestionResponse(questionId, questionAnswer.orEmpty()))
//        }
//        return answers
//    }

}
