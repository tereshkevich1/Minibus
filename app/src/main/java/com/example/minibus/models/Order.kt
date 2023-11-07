import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Int,
    val status: Int,
    val numberTickets: Int,
    val userId: Int,
    val tripId: Int,
    val departureStopId: Int,
    val arrivalStopId: Int
)