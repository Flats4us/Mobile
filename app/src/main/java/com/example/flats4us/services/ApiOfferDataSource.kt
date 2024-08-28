package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.DataStoreManager
import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.MapOffersResult
import com.example.flats4us.data.Offer
import com.example.flats4us.data.OffersResult
import com.example.flats4us.data.Rent
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.RentProposition
import com.example.flats4us.data.dto.NewOfferDto
import com.example.flats4us.data.dto.NewRentProposition
import com.example.flats4us.data.dto.OfferFilter
import com.example.flats4us.data.utils.RentResult
import com.example.flats4us.deserializer.MapOffersDeserializer
import com.example.flats4us.deserializer.OfferDeserializer
import com.example.flats4us.deserializer.OffersDeserializer
import com.example.flats4us.deserializer.RentResultDeserializer
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "ApiOfferDataSource"
object ApiOfferDataSource : OfferDataSource {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(OffersResult::class.java, OffersDeserializer())
        .registerTypeAdapter(MapOffersResult::class.java, MapOffersDeserializer())
        .registerTypeAdapter(Offer::class.java, OfferDeserializer())
        .registerTypeAdapter(RentResult::class.java, RentResultDeserializer())
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
            val offerService : OfferService = if(DataStoreManager.userRole.value != null){
                api
            } else {
                apiWithoutInterceptor
            }
            val response = offerService.getOffers(
                offerFilter.sorting,
                offerFilter.pageNumber,
                offerFilter.pageSize,
                offerFilter.province,
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_offers")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_offers")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_offers")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_offers")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_offers")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_offers")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_offer")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_offer")
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
                ApiResult.Success("success_added_rent_proposition")
            } else {
                Log.e(TAG, "Network error ${response.errorBody()?.string() ?: ""}")
                ApiResult.Error("error_failed_to_add_rent_proposition")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_rent_proposition")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_rent_proposition")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_rent_proposition")
        }
    }

    override suspend fun addRentDecision(offerId: Int, decision: Boolean): ApiResult<String> {
        return try {
            val requestBody = RentDecision(decision)
            Log.i(TAG, "Starting network request")
            val response = api.addRentDecision(offerId, requestBody)
            Log.i(TAG, "Request completed: ${response.isSuccessful}")
            if (response.isSuccessful) {
                val data = if (decision) {
                    "accepted_rent"
                } else {
                    "denied_rent"
                }
                Log.i(TAG, data)
                ApiResult.Success(data)
            } else {
                ApiResult.Error("error_failed_to_add_rent_decision")
            }
        } catch (e: CancellationException) {
            Log.e(TAG, "Job was cancelled", e)
            ApiResult.Error("internal_error_failed_to_add_rent_decision")
        } catch (e: Exception) {
            Log.e(TAG, "An internal error occurred", e)
            ApiResult.Error("internal_error_failed_to_add_rent_decision")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_offers")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_offers")
        }
    }

    override suspend fun createOffer(offer: NewOfferDto): ApiResult<String> {
        return try {
            val response = api.createOffer(offer)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success("offer_added_successfully")
            } else {
                ApiResult.Error("error_failed_to_create_offer")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_create_offer")
        }
    }

    override suspend fun addOfferToWatched(offerId: Int): ApiResult<String> {
        return try {
            val response = api.addOfferInterest(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success("success_added_offer_to_watched")
            } else {
                ApiResult.Error("error_failed_to_add_offer_to_watched")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_offer_to_watched")
        }
    }

    override suspend fun removeOfferToWatched(offerId: Int): ApiResult<String> {
        return try {
            val response = api.deleteOfferInterest(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success("success_removed_offer_from_watched")
            } else {
                ApiResult.Error("error_failed_to_remove_offer_from_watched")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_remove_offer_from_watched")
        }
    }

    override suspend fun getRents(): ApiResult<RentResult> {
        return try {
            val response = api.getRents()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_rents")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_rents")
        }
    }

    override suspend fun getRent(rentId: Int): ApiResult<Rent> {
        return try {

            val response = api.getRent(rentId)
            if(response.isSuccessful) {
                Log.d(TAG, "Response body: ${response.body()}")
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_rent")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_rent")
        }
    }

    override suspend fun cancelOffer(offerId: Int): ApiResult<String> {
        return try {
            val response = api.deleteOffer(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success("success_canceled_offer")
            } else {
                ApiResult.Error("error_cannot_cancel")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_cannot_cancel")
        }
    }

    override suspend fun promoteOffer(offerId: Int): ApiResult<String> {
        return try {
            val response = api.promoteOffer(offerId)
            if(response.isSuccessful) {
                val data = response.body()?.result ?: ""
                ApiResult.Success("success_promoted_offer")
            } else {
                ApiResult.Error("error_failed_to_promote_offer")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_promote_offer")
        }
    }

}