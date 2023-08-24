package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel

class RealEstateViewModel : ViewModel() {
    var voivodeship : String = ""
    var city : String = ""
    var district : String = ""
    var street : String = ""
    var buildingNumber : String = ""
    var area : Int = 0
    var maxResidents : Int = 0
    var constructionYear : Int = 0
    var numberOfRooms : Int = 0
    var numberOfFloors : Int = 0
    var equipment : String = ""
}