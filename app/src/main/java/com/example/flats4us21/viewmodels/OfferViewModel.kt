package com.example.flats4us21.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MapOffer
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.OfferFilter
import com.example.flats4us21.data.Property
import com.example.flats4us21.services.ApiOfferDataSource
import com.example.flats4us21.services.ApiPropertyDataSource
import com.example.flats4us21.services.OfferDataSource
import com.example.flats4us21.services.PropertyDataSource
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.ceil

private const val TAG = "OfferViewModel"
class OfferViewModel: ViewModel() {
    private val apiPropertyRepository : PropertyDataSource = ApiPropertyDataSource
    private val apiOfferRepository : OfferDataSource = ApiOfferDataSource

    private val _offers: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    val offers: LiveData<MutableList<Offer>>
    get() = _offers

    private val _mapOffers: MutableLiveData<MutableList<MapOffer>> = MutableLiveData()
    val mapOffers: LiveData<MutableList<MapOffer>>
    get() = _mapOffers



    private val _newOffers: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    val newOffers: LiveData<MutableList<Offer>>
    get() = _newOffers

    private val _offer: MutableLiveData<Offer?> = MutableLiveData()
    val offer: LiveData<Offer?>
    get() = _offer

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
    get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
    get() = _errorMessage

    private val _resultMessage = MutableLiveData<String?>(null)
    val resultMessage: LiveData<String?>
    get() = _resultMessage

    private val _userProperties: MutableLiveData<List<Property>> = MutableLiveData()
    val userProperties: LiveData<List<Property>>
    get() = _userProperties

    fun getUserProperties(showOnlyVerified: Boolean){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedProperties = apiPropertyRepository.getUserProperties(showOnlyVerified)
                Log.d(TAG, "[getUserProperties] Fetched properties: $fetchedProperties")
                when (fetchedProperties) {
                    is ApiResult.Success -> {
                        val data = fetchedProperties.data
                        _userProperties.value = data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedProperties.message
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

    private var _offersNumber : Int = 0
    var offersNumber: Int
    get() = _offersNumber
    set(value) {
        _offersNumber = value
    }

    private var _price : Int = 0
    var price: Int
    get() = _price
    set(value) {
        _price = value
    }

    private var _deposit : Int = 0
    var deposit: Int
    get() = _deposit
    set(value) {
        _deposit = value
    }

    private var _startDate : LocalDateTime? = null
    var startDate: LocalDateTime?
    get() = _startDate
    set(value) {
        _startDate = value
    }

    private var _endDate : LocalDateTime? = null
    var endDate: LocalDateTime?
    get() = _endDate
    set(value) {
        _endDate = value
    }

    private var _rentalPeriod: Int = 0
    var rentalPeriod: Int
    get() = _rentalPeriod
    set(value) {
        _rentalPeriod = value
    }

    private var _description: String = ""
    var description: String
    get() = _description
    set(value) {
        _description = value
    }

    private var _property: Property? = null
    var property: Property?
    get() = _property
    set(value) {
        _property = value
    }

    private var _rules: String = ""
    var rules: String
    get() = _rules
    set(value) {
        _rules = value
    }

    private var _smokingAllowed: Boolean = false
    var smokingAllowed: Boolean
    get() = _smokingAllowed
    set(value) {
        _smokingAllowed = value
    }

    private var _partiesAllowed: Boolean = false
    var partiesAllowed: Boolean
    get() = _partiesAllowed
    set(value) {
        _partiesAllowed = value
    }

    private var _animalsAllowed: Boolean = false
    var animalsAllowed: Boolean
    get() = _animalsAllowed
    set(value) {
        _animalsAllowed = value
    }

    private var _gender: Int = 0
    var gender: Int
    get() = _gender
    set(value) {
        _gender = value
    }

    private var _selectedOffer: Offer? = null
    var selectedOffer: Offer?
    get() = _selectedOffer
    set(value) {
        _selectedOffer = value
    }

    private var _sorting : String? = null
    var sorting : String?
    get() = _sorting
    set(value) {
        _sorting = value
    }
    private var _pageNumber : Int = 1
    var pageNumber : Int
    get() = _pageNumber
    set(value) {
        _pageNumber = value
    }
    private var _pageSize : Int = 5
    var pageSize : Int
    get() = _pageSize
    set(value) {
        _pageSize = value
    }
    private var _province : String? = null
    var province : String?
    get() = _province
    set(value) {
        _province = value
    }
    private var _city : String? = null
    var city : String?
    get() = _city
    set(value) {
        _city = value
    }
    private var _distance : Int? = null
    var distance : Int?
    get() = _distance
    set(value) {
        _distance = value
    }
    private var _propertyType : Int? = null
    var propertyType : Int?
    get() = _propertyType
    set(value) {
        _propertyType = value
    }
    private var _minPrice : Int? = null
    var minPrice : Int?
    get() = _minPrice
    set(value) {
        _minPrice = value
    }
    private var _maxPrice : Int? = null
    var maxPrice : Int?
    get() = _maxPrice
    set(value) {
        _maxPrice = value
    }
    private var _district : String? = null
    var district : String?
    get() = _district
    set(value) {
        _district = value
    }
    private var _minArea : Int? = null
    var minArea : Int?
    get() = _minArea
    set(value) {
        _minArea = value
    }
    private var _maxArea : Int? = null
    var maxArea : Int?
    get() = _maxArea
    set(value) {
        _maxArea = value
    }
    private var _minYear : Int? = null
    var minYear : Int?
    get() = _minYear
    set(value) {
        _minYear = value
    }
    private var _maxYear : Int? = null
    var maxYear : Int?
    get() = _maxYear
    set(value) {
        _maxYear = value
    }
    private var _minNumberOfRooms : Int? = null
    var minNumberOfRooms : Int?
    get() = _minNumberOfRooms
    set(value) {
        _minNumberOfRooms = value
    }
    private var _floor : Int? = null
    var floor : Int?
    get() = _floor
    set(value) {
        _floor = value
    }
    private var _equipment : List<Int>? = null
    var equipment : List<Int>?
    get() = _equipment
    set(value) {
        _equipment = value
    }

    fun getOffer(offerId : Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.getOffer(offerId)) {
                    is ApiResult.Success -> {
                        _offer.value = response.data
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

    fun createOffer(callback: (Boolean) -> Unit){
        val offer = NewOfferDto(
            property!!.propertyId,
            price,
            deposit,
            description,
            startDate.toString(),
            endDate.toString(),
            rules,
            smokingAllowed,
            partiesAllowed,
            animalsAllowed,
            gender,
        )
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.createOffer(offer)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, " Created property")
                        _resultMessage.value = response.data
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


    fun getOffers() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val filter = OfferFilter(
                    null,
                    pageNumber,
                    pageSize,
                    province,
                    city,
                    distance,
                    propertyType,
                    minPrice,
                    maxPrice,
                    district,
                    minArea,
                    maxArea,
                    minYear,
                    maxYear,
                    minNumberOfRooms,
                    floor,
                    equipment
                )
                if(pageNumber == 1 && _offers.value != null) {
                    _offers.value!!.clear()
                }
                Log.i(TAG, "Filter: $filter")
                val fetchedOffers = apiOfferRepository.getOffers(filter)
                Log.i(TAG, "Fetched offers: $fetchedOffers")
                when (fetchedOffers) {
                    is ApiResult.Success -> {
                        val data = fetchedOffers.data.result as MutableList<Offer>
                        val totalCount = fetchedOffers.data.totalCount
                        _newOffers.value = data
                        _offersNumber = totalCount
                        pageNumber++

                        if(_offers.value == null) {
                            _offers.value = data
                        } else {
                            _offers.value!!.addAll(data)
                        }
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedOffers.message
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

    fun getOffersForMap() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val filter = OfferFilter(
                    null,
                    pageNumber,
                    pageSize,
                    province,
                    city,
                    distance,
                    propertyType,
                    minPrice,
                    maxPrice,
                    district,
                    minArea,
                    maxArea,
                    minYear,
                    maxYear,
                    minNumberOfRooms,
                    floor,
                    equipment
                )
                if(pageNumber == 1 && _offers.value != null) {
                    _offers.value!!.clear()
                }
                Log.i(TAG, "Filter: $filter")
                val fetchedOffers = apiOfferRepository.getOffersForMap(filter)
                Log.i(TAG, "Fetched offers: $fetchedOffers")
                when (fetchedOffers) {
                    is ApiResult.Success -> {
                        val data = fetchedOffers.data.result as MutableList<MapOffer>

                        if(_offers.value == null) {
                            _mapOffers.value = data
                        }
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedOffers.message
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

    fun getMineOffers() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try{
                val fetchedOffers = apiOfferRepository.getMineOffers()
                Log.i(TAG, "Fetched offers: $fetchedOffers")
                when (fetchedOffers) {
                    is ApiResult.Success -> {
                        val data = fetchedOffers.data as MutableList<Offer>
                        _offers.value = data
                    }
                    is ApiResult.Error -> {
                        val errorMessage = fetchedOffers.message
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

    fun watchOffer(offerId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _resultMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.addOfferToWatched(offerId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, response.data)
                        _resultMessage.value = response.data
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

    fun unwatchOffer(offerId: Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _resultMessage.value = null
            _isLoading.value = true
            try {
                when (val response = apiOfferRepository.removeOfferToWatched(offerId)) {
                    is ApiResult.Success -> {
                        Log.d(TAG, response.data)
                        _resultMessage.value = response.data
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

    fun clearNullableVariables() {
        _sorting = null
        _province = null
        _city = null
        _distance = null
        _pageNumber = 1
        _propertyType = null
        _minPrice = null
        _maxPrice = null
        _district = null
        _minArea = null
        _maxArea = null
        _minYear = null
        _maxYear = null
        _minNumberOfRooms = null
        _floor = null
        _equipment = null
    }
}