package com.example.flats4us21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.SurveyQuestion

class MainViewModel : ViewModel() {
    private val questionList = MutableLiveData<List<SurveyQuestion>>()

    fun setQuestions(list: List<SurveyQuestion>){
        this.questionList.value = list
    }

    fun getQuestionList() : LiveData<List<SurveyQuestion>>{
        return this.questionList
    }
}