package com.example.flats4us21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.services.HardcodedOfferDataSource
import com.example.flats4us21.services.OfferDataSource


interface Server {
    fun loadDataFromDb() : List<Offer>
}

class MainViewModel : ViewModel(), Server {
    private val offerRepository : OfferDataSource = HardcodedOfferDataSource
    private var selectedOffer: Offer? = null
    private val questionList = MutableLiveData<List<SurveyQuestion>>()


    override fun loadDataFromDb(): List<Offer> {
        return offerRepository.getOffers()
    }


    fun setOffer(offer: Offer){
        this.selectedOffer = offer
    }
    fun getOffer() = selectedOffer


    fun setQuestions(list: List<SurveyQuestion>){
        this.questionList.value = list
    }

    fun getQuestionList() : LiveData<List<SurveyQuestion>>{
        return this.questionList
    }


}