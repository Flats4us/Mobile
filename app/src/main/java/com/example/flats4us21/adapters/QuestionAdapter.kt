package com.example.flats4us21.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.data.SurveyQuestion

class QuestionAdapter(
    private val questions: List<SurveyQuestion>,
    private val initialResponses: Map<String, Any>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userResponses: MutableMap<String, Any> = initialResponses?.toMutableMap() ?: mutableMapOf()
    private var triggerQuestionActive = false

    init {
        updateTriggerStatus()
    }

    private fun updateTriggerStatus() {
        val triggerQuestion = questions.find { it.trigger }
        triggerQuestionActive = triggerQuestion != null && (userResponses[triggerQuestion.name] as? Boolean ?: false)
    }

    private fun filteredQuestions(): List<SurveyQuestion> {
        updateTriggerStatus()
        return questions.filter { it.optional == false || (it.optional == true && triggerQuestionActive) }
    }

    override fun getItemViewType(position: Int): Int {
        return when (filteredQuestions()[position].typeName) {
            "TEXT" -> R.layout.item_question_text
            "NUMBER" -> R.layout.item_question_number
            "SLIDER" -> R.layout.item_question_slider
            "SWITCH" -> R.layout.item_question_switch
            "RADIOBUTTON" -> R.layout.item_question_radiobutton
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_question_text -> TextViewHolder(view)
            R.layout.item_question_number -> NumberViewHolder(view)
            R.layout.item_question_slider -> SliderViewHolder(view)
            R.layout.item_question_switch -> SwitchViewHolder(view)
            R.layout.item_question_radiobutton -> RadioButtonViewHolder(view)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = filteredQuestions()[position]
        when (holder) {
            is TextViewHolder -> holder.bind(question)
            is NumberViewHolder -> holder.bind(question)
            is SliderViewHolder -> holder.bind(question)
            is SwitchViewHolder -> holder.bind(question)
            is RadioButtonViewHolder -> holder.bind(question)
        }
    }

    override fun getItemCount(): Int = filteredQuestions().size

    fun getUserResponses(): Map<String, Any> = userResponses

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val editText: EditText = view.findViewById(R.id.answerEditText)
        private val title: TextView = view.findViewById(R.id.questionName)

        fun bind(question: SurveyQuestion) {
            title.text = question.name
            editText.hint = question.name
            editText.setText(userResponses[question.name]?.toString() ?: "")
            editText.addTextChangedListener {
                userResponses[question.name] = it.toString()
            }
        }
    }

    inner class NumberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val editText: EditText = view.findViewById(R.id.answerEditText)
        private val title: TextView = view.findViewById(R.id.questionName)
        fun bind(question: SurveyQuestion) {
            title.text = question.name
            editText.hint = question.name
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setText(userResponses[question.name]?.toString() ?: "")
            editText.addTextChangedListener {
                userResponses[question.name] = it.toString().toIntOrNull() ?: 0
            }
        }
    }

    inner class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val slider: SeekBar = view.findViewById(R.id.answerSlider)
        private val title: TextView = view.findViewById(R.id.questionName)

        fun bind(question: SurveyQuestion) {
            title.text = question.name
            val min = question.answers[0].toInt()
            val max = question.answers[1].toInt()
            slider.max = max - min
            slider.progress = (userResponses[question.name] as? Int ?: min) - min

            slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    userResponses[question.name] = min + progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }


    inner class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: Switch = view.findViewById(R.id.answerSwitch)
        private val title: TextView = view.findViewById(R.id.questionName)
        fun bind(question: SurveyQuestion) {
            title.text = question.name
            switch.isChecked = userResponses[question.name] as? Boolean ?: false
            if (!userResponses.containsKey(question.name)) {
                userResponses[question.name] = switch.isChecked
            }
            switch.setOnCheckedChangeListener { _, isChecked ->
                userResponses[question.name] = isChecked
                notifyDataSetChanged()
            }
        }
    }

    inner class RadioButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val radioGroup: RadioGroup = view.findViewById(R.id.answerRadioGroup)
        private val title: TextView = view.findViewById(R.id.questionName)
        fun bind(question: SurveyQuestion) {
            title.text = question.name
            radioGroup.removeAllViews()
            question.answers.forEachIndexed { index, answer ->
                val radioButton = RadioButton(itemView.context).apply {
                    text = answer
                    id = index
                }
                radioGroup.addView(radioButton)
            }
            radioGroup.check(userResponses[question.name] as? Int ?: -1)
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                userResponses[question.name] = checkedId
            }
        }
    }
}
