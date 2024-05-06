package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.*
import com.example.flats4us21.data.dto.NewOfferDto
import com.example.flats4us21.data.dto.OfferFilter
import com.example.flats4us21.deserializer.OfferDeserializer
import com.example.flats4us21.deserializer.OffersDeserializer
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiOfferDataSource"
object ApiOfferDataSource : OfferDataSource {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(OffersResult::class.java, OffersDeserializer())
        .registerTypeAdapter(Offer::class.java, OfferDeserializer())
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
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
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OfferService::class.java)
    }

    override suspend fun getOffers(offerFilter: OfferFilter): ApiResult<List<Offer>> {
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
            Log.d(TAG, "Response Headers: ${response.headers()}")
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
            val response = apiWithoutInterceptor.getOffer(offerId)
            if(response.isSuccessful) {
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
        rentProposition: RealEstateRental
    ): ApiResult<String> {
        return try {
            val response = api.addRentProposition(offerId, rentProposition)
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

    override suspend fun getWatchedOffers(): List<Offer> {
        TODO("Not yet implemented")
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

    override fun addOfferToWatched(offer: Offer) {
        TODO("Not yet implemented")
    }

    override fun removeOfferToWatched(offer: Offer) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastViewedOffers(): List<Offer> {
        TODO("Not yet implemented")
    }

    override fun addOfferToLastViewed(offer: Offer) {
        TODO("Not yet implemented")
    }
}