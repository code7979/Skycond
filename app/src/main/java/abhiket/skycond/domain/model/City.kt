package abhiket.skycond.domain.model

data class City(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val state: String?,
    val country: String?,
)