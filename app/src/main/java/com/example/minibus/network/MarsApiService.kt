package com.example.minibus.network

import com.example.minibus.models.City
import com.example.minibus.models.Details
import com.example.minibus.models.Driver
import com.example.minibus.models.StopPoint
import com.example.minibus.models.Transport
import com.example.minibus.models.TripTime
import com.example.minibus.models.User
import com.example.minibus.models.UserTravelHistory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate
import java.time.LocalTime

private const val BASE_URL =
    "http://192.168.100.2:8080"

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

    @GET("driver/{id}")
    suspend fun getDriverById(
        @Path("id") driverId: Int
    ): Driver

    @GET("orderDetails/{id}")
    suspend fun getOrderDetails(
        @Path("id") id: Int
    ): Details

    @GET("stops/{city_id}")
    suspend fun getStopsByCityId(
        @Path("city_id") cityId: Int
    ): List<StopPoint>

    @GET("userHistory/{user_id}")
    suspend fun getUserTravelHistory(
        @Path("user_id") cityId: Int
    ): List<UserTravelHistory>

    @GET("transport/{minibus_id}")
    suspend fun getTransport(
        @Path("minibus_id") minibusId: Int
    ): Transport

    @POST("user/{id}/{firstName}/{lastName}/{phoneNumber}/update")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Path("firstName") firstName: String,
        @Path("lastName") lastName: String,
        @Path("phoneNumber") phoneNumber: String,
    ): Response<Void>


    @POST("order/{id}/delete")
    suspend fun deleteOrder(
        @Path("id") orderId: Int,
    ): Response<Void>

    @POST("/order/add/{userId}/{tripId}/{numberTickets}/{status}/{departureStopId}/{arrivalStopId}")
    suspend fun addOrder(
        @Path("userId") userId: Int,
        @Path("tripId") tripId: Int,
        @Path("numberTickets") numberTickets: Int,
        @Path("status") status: Int,
        @Path("departureStopId") departureStopId: Int,
        @Path("arrivalStopId") arrivalStopId: Int,
    ): Response<Void>

    @GET("user/{phoneNumber}/{password}/logIn")
    fun logIn(
        @Path("password") password: String,
        @Path("phoneNumber") phoneNumber: String
    ): Call<User>

    @POST("/user/{firstName}/{lastName}/{phoneNumber}/{password}/addNewUser")
    fun signUp(
        @Path("firstName") firstName: String,
        @Path("lastName") lastName: String,
        @Path("phoneNumber") phoneNumber: String,
        @Path("password") password: String
    ): Call<User>

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

object JsonFormat {
    val instance: Json = Json {
        serializersModule = SerializersModule {
            contextual(LocalDate::class, LocalDateSerializer)
            contextual(LocalTime::class, LocalTimeSerializer)
        }
    }
}

