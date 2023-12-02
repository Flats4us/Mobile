package com.example.flats4us21.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.Equipment
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.services.*
import kotlinx.coroutines.launch

private const val TAG = "RealEstateViewModel"
class RealEstateViewModel : ViewModel() {
    private val placeRepository : PlaceDataSource = HardcodedPlaceDataSource()
    private val equipmentRepository : EquipmentDataSource = ApiEquipmentDataSource
    private val propertyRepository : PropertyDataSource = ApiPropertyDataSource
    val voivodeshipSuggestions = ArrayList<String>()

    fun fetchVoivodeships() {
        val voivodeships = placeRepository.getVoivodeships()
        voivodeshipSuggestions.addAll(voivodeships)
    }

    fun getDistricts(city: String): MutableList<String>{
        return placeRepository.getDistricts(city)
    }

    private val _equipments = MutableLiveData<List<Equipment>>()
    val equipments: LiveData<List<Equipment>>
        get() = _equipments

    fun getEquipmentList() {
        _isLoading.value = true
        viewModelScope.launch {
            val fetchedEquipments = equipmentRepository.getEquipment()
            Log.i(TAG, "Fetched list of equipment: $fetchedEquipments")
            _equipments.value = fetchedEquipments
            _isLoading.value = false
        }
    }

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

    private var _floor: String = ""
    var floor: String
        get() = _floor
        set(value) {
            _floor = value
        }

    private var _flatNumber: String = ""
    var flatNumber: String
        get() = _flatNumber
        set(value) {
            _flatNumber = value
        }

    private var _area: Int? = 0
    var area: Int?
        get() = _area
        set(value) {
                _area = value
        }

    private var _landArea: Int? = 0
    var landArea: Int?
        get() = _landArea
        set(value) {
                _landArea = value
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

    private var _numberOfRooms: Int? = 0
    var numberOfRooms: Int?
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

    private var _equipment: MutableList<Int> = mutableListOf()
    var equipment: MutableList<Int>
        get() = _equipment
        set(value) {
            _equipment = value
        }

    private var _images: MutableList<Uri> = mutableListOf()
    var images: MutableList<Uri>
        get() = _images
        set(value) {
            _images = value
        }

    private var _ownerId: Int = 1
    var ownerId : Int
        get() = _ownerId
        set(value) {
            _ownerId = value
        }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun createRealEstateObject() {
        val newProperty = NewPropertyDto(
            PropertyType.valueOf(propertyType!!),
            voivodeship,
            city,
            district,
            street,
            buildingNumber,
            flatNumber,
            area!!,
            landArea,
            maxResidents,
            constructionYear,
            false,
            numberOfRooms!!,
            equipment,
            images,
            ownerId
        )
        viewModelScope.launch{
            _errorMessage.value = null
            _isLoading.value = true
            try {
                propertyRepository.addProperty(newProperty)
                Log.d(TAG, "New Property to create: $newProperty")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProperty(property: Property){
        TODO("Not yet implemented")
    }
}
