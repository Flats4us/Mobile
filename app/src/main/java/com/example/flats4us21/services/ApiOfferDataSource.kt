package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MapOffersResult
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.OffersResult
import com.example.flats4us21.data.RentProposition
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.NewRentProposition
import com.example.flats4us21.data.dto.OfferFilter
import com.example.flats4us21.deserializer.MapOffersDeserializer
import com.example.flats4us21.deserializer.OfferDeserializer
import com.example.flats4us21.deserializer.OffersDeserializer
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiOfferDataSource"
object ApiOfferDataSource : OfferDataSource {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(OffersResult::class.java, OffersDeserializer())
        .registerTypeAdapter(MapOffersResult::class.java, MapOffersDeserializer())
        .registerTypeAdapter(Offer::class.java, OfferDeserializer())
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val okHttpClientWithoutInterceptor = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: OfferService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OfferService::class.java)
    }

    private val apiWithoutInterceptor: OfferService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClientWithoutInterceptor)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OfferService::class.java)
    }

    override suspend fun getOffers(offerFilter: OfferFilter): ApiResult<OffersResult> {
        return try {
            val response = apiWithoutInterceptor.getOffers(
                offerFilter.sorting,
                offerFilter.pageNumber,
                offerFilter.pageSize,
                offerFilter.city,
                offerFilter.distnace,
                offerFilter.propertyType,
                offerFilter.minPrice,
                offerFilter.maxPrice,
                offerFilter.district,
                offerFilter.minArea,
                offerFilter.maxArea,
                offerFilter.minYear,
                offerFilter.maxYear,
                offerFilter.minNumberOfRooms,
                offerFilter.floor,
                offerFilter.equipment
            )
            Log.d(TAG, "Response status: ${response.isSuccessful}")
            if(response.isSuccessful) {
                Log.d(TAG, "Response body: ${response.body()}")
                val data = response.body()
                if (data != null) {
                        ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getOffersForMap(offerFilter: OfferFilter): ApiResult<MapOffersResult> {
        return try {
            val response = apiWithoutInterceptor.getOffersForMap(
                offerFilter.city,
                offerFilter.distnace,
                offerFilter.propertyType,
                offerFilter.minPrice,
                offerFilter.maxPrice,
                offerFilter.district,
                offerFilter.minArea,
                offerFilter.maxArea,
                offerFilter.minYear,
                offerFilter.maxYear,
                offerFilter.minNumberOfRooms,
                offerFilter.floor,
                offerFilter.equipment
            )
            Log.d(TAG, "Response status: ${response.isSuccessful}")
            if(response.isSuccessful) {
                Log.d(TAG, "Response body: ${response.body()}")
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getMineOffers(): ApiResult<List<Offer>> {
        return try {
            val response = api.getMineOffers()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data.result)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getOffer(offerId: Int): ApiResult<Offer> {
        return try {
            val offerService : OfferService = if(DataStoreManager.userRole.value != null){
                api
            } else {
                apiWithoutInterceptor
            }
            val response = offerService.getOffer(offerId)
            if(response.isSuccessful) {
                Log.d(TAG, "Response body: ${response.body()}")
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun addRentProposition(
        offerId: Int,
        rentProposition: NewRentProposition
    ): ApiResult<String> {
        return try {
            val response = api.addRentProposition(offerId, rentProposition)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success(data)
            } else {
                Log.e(TAG, "Network error ${response.errorBody()?.string() ?: ""}")
                ApiResult.Error("Failed to rent proposition: ${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getRentProposition(rentId: Int): ApiResult<RentProposition> {
        return try {
            val response = api.getRentProposition(rentId)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch rent proposition: $${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun addRentDecision(offerId: Int, decision: Boolean): ApiResult<String> {
        return try {
            Log.i(TAG, "Before json transformation")
            val jsonObject = JSONObject()
            jsonObject.put("decision", decision)
            val json = jsonObject.toString()
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
            Log.i(TAG, "Before api request")
            val response = api.addRentDecision(offerId, requestBody)
            Log.i(TAG, "is successful: ${response.isSuccessful}")
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                Log.i(TAG, "Response body: $data")
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getWatchedOffers(pageNumber: Int, pageSize: Int): ApiResult<OffersResult> {
        return try {
            val response = api.getObservedOffers(pageNumber, pageSize)
            if(response.isSuccessful) {
                Log.d(TAG, "Response body: ${response.body()}")
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun createOffer(offer: NewOfferDto): ApiResult<String> {
        return try {
            val response = api.createOffer(offer)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun addOfferToWatched(offerId: Int): ApiResult<String> {
        return try {
            val response = api.addOfferInterest(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to add offer to watched: ${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun removeOfferToWatched(offerId: Int): ApiResult<String> {
        return try {
            val response = api.addOfferInterest(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to delete offer from watched: ${response.errorBody()?.string() ?: ""}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getLastViewedOffers(): List<Offer> {
        TODO("Not yet implemented")
    }

    override fun addOfferToLastViewed(offer: Offer) {
        TODO("Not yet implemented")
    }
}