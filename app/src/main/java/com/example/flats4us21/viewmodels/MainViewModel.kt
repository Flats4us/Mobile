package com.example.flats4us21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flats4us21.R
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.SurveyQuestion

private val offer1 =
    Offer(
        dateIssue = "20-03-2023",
        status = "aktywny",
        price = "%.2f".format(2500.50F),
        description = "Lorem ipsum",
        rentalPeriod = "3 lata",
        interestedPeople = 48,
        Property(
            city = "Warszawa",
            street = "Kościuszki 10A",
            maxResidents = 2,
            equipment = "Brak",
            area = 40,
            image = listOf(R.drawable.property),
            numberOfRooms = 1
        )
    )
private val offer2 =
    Offer(
        dateIssue = "21-03-2023",
        status = "aktywny",
        price = "%.2f".format(3500.50F),
        description = "Lorem ipsum",
        rentalPeriod = "2 lata",
        interestedPeople = 18,
        Property(
            city = "Warszawa",
            street = "Pruszkowska 10A",
            maxResidents = 4,
            equipment = "Sofa",
            area = 60,
            image = listOf(R.drawable.property),
            numberOfRooms = 3
        )
    )
private val offer3 =
    Offer(
        dateIssue = "21-03-2023",
        status = "aktywny",
        price = "%.2f".format(3500.50F),
        description = "Lorem ipsum",
        rentalPeriod = "2 lata",
        interestedPeople = 18,
        Property(
            city = "Warszawa",
            street = "Pruszkowska 10A",
            maxResidents = 4,
            equipment = "Sofa",
            area = 50,
            image = listOf(R.drawable.property),
            numberOfRooms = 2
        )
    )
private val data = listOf(offer1, offer2, offer3)

interface Server {
    fun loadDataFromDb() : List<Offer>
}

class MainViewModel : ViewModel(), Server {
    private var selectedOffer: Offer? = null
    private val questionList = MutableLiveData<List<SurveyQuestion>>()


    override fun loadDataFromDb(): List<Offer> {
        return data
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