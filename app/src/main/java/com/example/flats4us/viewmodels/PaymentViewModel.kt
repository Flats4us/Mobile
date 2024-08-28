package com.example.flats4us.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flats4us.data.StudentsPayment
import com.example.flats4us.services.ApiStudentsPaymentDataSource
import com.example.flats4us.services.StudentsPaymentDataSource
import kotlinx.coroutines.launch

private const val TAG = "PaymentViewModel"
class PaymentViewModel: ViewModel()  {
    private val paymentRepository : StudentsPaymentDataSource = ApiStudentsPaymentDataSource

    private val _payment = MutableLiveData<StudentsPayment?>(null)
    val payment: LiveData<StudentsPayment?>
        get() = _payment

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun getStudentsPayment(paymentId : Int){
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedProperties = paymentRepository.getStudentsPayment(paymentId)
                Log.d(TAG, "[getUserProperties] Fetched properties: $fetchedProperties")
                _payment.value = fetchedProperties
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
}