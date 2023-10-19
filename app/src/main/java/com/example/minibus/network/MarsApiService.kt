package com.example.minibus.network

import com.example.marsphotos.model.City
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    "http://192.168.100.3:8080"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service object for creating api calls
 */
interface MinibusApiService {
    @GET("city")
    suspend fun getPhotos(): List<City>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MinibusApi {
    val retrofitService: MinibusApiService by lazy {
        retrofit.create(MinibusApiService::class.java)
    }
}
