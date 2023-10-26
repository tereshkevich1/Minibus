package com.example.minibus.network

import com.example.minibus.models.City
import com.example.minibus.models.TripTime
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.time.LocalTime

private const val BASE_URL =
    "http://192.168.100.3:8080"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val jsonFormat = Json {
    serializersModule = SerializersModule {
        contextual(LocalDate::class, LocalDateSerializer)
        contextual(LocalTime::class, LocalTimeSerializer)
    }
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service object for creating api calls
 */
interface MinibusApiService {
    @GET("city")
    suspend fun getPhotos(): List<City>

    @GET("trip/{startingLocationId}/{finalLocationId}/{departureDate}")
    suspend fun getTrips(
        @Path("startingLocationId") startingLocationId: Int,
        @Path("finalLocationId") finalLocationId: Int,
        @Path("departureDate") departureDate: String
    ): List<TripTime>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MinibusApi {
    val retrofitService: MinibusApiService by lazy {
        retrofit.create(MinibusApiService::class.java)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalTime::class)
object LocalTimeSerializer : KSerializer<LocalTime> {
    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}

