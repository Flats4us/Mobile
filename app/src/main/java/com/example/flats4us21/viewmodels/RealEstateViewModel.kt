package com.example.flats4us21.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.CSVUtils
import com.example.flats4us21.data.Equipment
import com.example.flats4us21.data.Image
import com.example.flats4us21.data.Place
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.dto.NewPropertyDto
import com.example.flats4us21.data.dto.NewRentOpinionDto
import com.example.flats4us21.services.ApiEquipmentDataSource
import com.example.flats4us21.services.ApiPropertyDataSource
import com.example.flats4us21.services.EquipmentDataSource
import com.example.flats4us21.services.HardcodedPlaceDataSource
import com.example.flats4us21.services.PlaceDataSource
import com.example.flats4us21.services.PropertyDataSource
import kotlinx.coroutines.launch
import java.io.File

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

    private val _property = MutableLiveData<Property>()
    val property: LiveData<Property>
        get() = _property

    private val _equipments = MutableLiveData<List<Equipment>>()
    val equipments: LiveData<List<Equipment>>
        get() = _equipments

    private var _newRentOpinionDto: NewRentOpinionDto? = null
    var newRentOpinionDto: NewRentOpinionDto?
        get() = _newRentOpinionDto
        set(value) {
            _newRentOpinionDto = value
        }

    private var _isCreating: Boolean = true
    var isCreating: Boolean
        get() = _isCreating
        set(value) {
            _isCreating = value
        }

    private var _propertyId: Int = 0
    var propertyId: Int
        get() = _propertyId
        set(value) {
            _propertyId = value
        }

    private var _propertyType: PropertyType? = null
    var propertyType: PropertyType?
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

    private var _postalCode: String = ""
    var postalCode: String
        get() = _postalCode
        set(value) {
            _postalCode = value
        }

    private var _district: String? = null
    var district: String?
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

    private var _floor: Int? = 0
    var floor: Int?
        get() = _floor
        set(value) {
            _floor = value
        }

    private var _flatNumber: String? = ""
    var flatNumber: String?
        get() = _flatNumber
        set(value) {
            _flatNumber = value
        }

    private var _area: Int = 0
    var area: Int
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

    private var _numberOfFloors: Int? = 0
    var numberOfFloors: Int?
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

    private var _imagesURI: MutableList<Uri> = mutableListOf()
    var imagesURI: MutableList<Uri>
        get() = _imagesURI
        set(value) {
            _imagesURI = value
        }

    private var _images: MutableList<Image> = mutableListOf()
    var images: MutableList<Image>
        get() = _images
        set(value) {
            _images = value
        }

    private var _imageFiles: MutableList<File> = mutableListOf()
    var imageFiles: MutableList<File>
        get() = _imageFiles
        set(value) {
            _imageFiles = value
        }

    private var _titleDeedFile: File? = null
    var titleDeedFile: File?
        get() = _titleDeedFile
        set(value) {
            _titleDeedFile = value
        }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _selectedProperty: Property? = null
    var selectedProperty: Property?
        get() = _selectedProperty
        set(value) {
            _selectedProperty = value
        }

    fun getEquipmentList() {
        _isLoading.value = true
        viewModelScope.launch {
            val fetchedEquipments = equipmentRepository.getEquipment()
            Log.i(TAG, "Fetched list of equipment: $fetchedEquipments")
            _equipments.value = fetchedEquipments
            _isLoading.value = false
        }
    }

    fun createProperty(callback: (String?) -> Unit) {
        val newProperty = NewPropertyDto(
            propertyType.toString().toInt(),
            voivodeship,
            district,
            street,
            buildingNumber,
            flatNumber,
            city,
            postalCode,
            area,
            maxResidents,
            constructionYear,
            numberOfRooms,
            floor,
            numberOfFloors,
            landArea,
            equipment
        )
        Log.i(TAG, newProperty.toString())
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = propertyRepository.addProperty(newProperty)) {
                    is ApiResult.Success -> {
                        val propertyId = response.data
                        Log.i(TAG, "BEFORE add files to property")
                        val filesResponse = propertyRepository.addFilesToProperty(propertyId, titleDeedFile!!, imageFiles)
                        when (filesResponse) {
                            is ApiResult.Success -> {
                                val message = filesResponse.data
                                _isLoading.value = false
                                Log.d(TAG, "New Property to create: $newProperty")
                                callback(message)
                            }
                            is ApiResult.Error -> {
                                val fileErrorMessage = filesResponse.message
                                Log.e(TAG, "Error adding files to property: $fileErrorMessage")
                                _errorMessage.value = fileErrorMessage
                                callback(null)
                            }
                        }
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error adding property: $errorMessage")
                        _errorMessage.value = errorMessage
                        callback(null)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
                callback(null)
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun updateProperty() {
        val property = NewPropertyDto(
            propertyType.toString().toInt(),
            voivodeship,
            district,
            street,
            buildingNumber,
            flatNumber,
            city,
            postalCode,
            area,
            maxResidents,
            constructionYear,
            numberOfRooms,
            floor,
            numberOfFloors,
            landArea,
            equipment
        )
        viewModelScope.launch{
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when(val response = propertyRepository.updateProperty(propertyId, property)) {
                    is ApiResult.Success -> {
                        Log.i(TAG, response.data.result)
                    }
                    is ApiResult.Error -> {
                        Log.e(TAG, response.message)
                        _errorMessage.value = response.message
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getProperty(propertyId: Int) {
        viewModelScope.launch{
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = propertyRepository.getProperty(propertyId)) {
                    is ApiResult.Success -> {
                        val fetchedProperty = response.data
                        _property.value = fetchedProperty
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProperty(propertyId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch{
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = propertyRepository.deleteProperty(propertyId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, " Deleted property: $propertyId")
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
                callback(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRentOpinion(rentId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch{
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val response = propertyRepository.addRentOpinion(rentId, _newRentOpinionDto!!)
                when (response) {
                    is ApiResult.Success -> {
                        Log.d(TAG, " Deleted property: $propertyId")
                        callback(true)
                    }
                    is ApiResult.Error -> {
                        val errorMessage = response.message
                        Log.e(TAG, "Error: $errorMessage")
                        _errorMessage.value = errorMessage
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
                callback(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearProperties() {
        _propertyType = null
        _voivodeship = ""
        _city = ""
        _postalCode = ""
        _district = null
        _street = ""
        _buildingNumber = ""
        _floor = 0
        _flatNumber = ""
        _area = 0
        _landArea = 0
        _maxResidents = 0
        _constructionYear = 0
        _numberOfRooms = 0
        _numberOfFloors = 0
        _equipment.clear()
        _imagesURI.clear()
        _images.clear()
        _imageFiles.clear()
        _titleDeedFile = null
    }

    private val cities: MutableLiveData<List<Place>> = MutableLiveData()

    fun loadCities(context: Context) {
        val loadedCities = CSVUtils.loadCities(context, "wojewodztwa_miasta.csv")
        Log.d(TAG, "Loaded cities: $loadedCities")
        cities.value = loadedCities
        Log.d(TAG, "Cities: ${cities.value}")
    }

    fun getCitiesByVoivodeship(voivodeship: String): List<String> {
        Log.d(TAG, "voivodeship: $voivodeship")
        val cities = cities.value!!.filter { it.voivodeship == voivodeship.uppercase() }.map { it.name }
        Log.d(TAG, "Cities: $cities")
        return cities
    }

}
