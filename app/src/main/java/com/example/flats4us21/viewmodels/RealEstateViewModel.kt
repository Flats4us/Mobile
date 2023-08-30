package com.example.flats4us21.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Property

class RealEstateViewModel : ViewModel() {

    private var _propertyType: String? = null
    var propertyType: String?
        get() = _propertyType
        set(value) {
            _propertyType = value
        }

    private var _voivodeship: String = ""
    var voivodeship: String
        get() = _voivodeship
        set(value) {
            _voivodeship = value
        }

    private var _city: String = ""
    var city: String
        get() = _city
        set(value) {
            _city = value
        }

    private var _district: String = ""
    var district: String
        get() = _district
        set(value) {
            _district = value
        }

    private var _street: String = ""
    var street: String
        get() = _street
        set(value) {
            _street = value
        }

    private var _buildingNumber: String = ""
    var buildingNumber: String
        get() = _buildingNumber
        set(value) {
            _buildingNumber = value
        }

    private var _area: Int = 0
    var area: Int
        get() = _area
        set(value) {
                _area = value
        }

    private var _maxResidents: Int = 0
    var maxResidents: Int
        get() = _maxResidents
        set(value) {
                _maxResidents = value
        }

    private var _constructionYear: Int = 0
    var constructionYear: Int
        get() = _constructionYear
        set(value) {
                _constructionYear = value
        }

    private var _numberOfRooms: Int = 0
    var numberOfRooms: Int
        get() = _numberOfRooms
        set(value) {
                _numberOfRooms = value
        }

    private var _numberOfFloors: Int = 0
    var numberOfFloors: Int
        get() = _numberOfFloors
        set(value) {
                _numberOfFloors = value
        }

    private var _equipment: String = ""
    var equipment: String
        get() = _equipment
        set(value) {
            _equipment = value
        }

    private var _imageUris: MutableList<Uri> = mutableListOf()
    var imageUris: MutableList<Uri>
        get() = _imageUris
        set(value) {
            _imageUris = value
        }

//    fun createRealEstateObject(): Property {
//        return Property(
//            propertyType = propertyType ?: "",
//            voivodeship = voivodeship,
//            city = city,
//            district = district,
//            street = street,
//            buildingNumber = buildingNumber,
//            area = area,
//            maxResidents = maxResidents,
//            constructionYear = constructionYear,
//            numberOfRooms = numberOfRooms,
//            numberOfFloors = numberOfFloors,
//            equipment = equipment
//        )
//    }
}
