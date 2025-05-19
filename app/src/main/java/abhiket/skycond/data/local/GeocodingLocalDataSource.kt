package abhiket.skycond.data.local

import abhiket.City
import app.cash.sqldelight.Query

interface GeocodingLocalDataSource {
    suspend fun getCities(): Query<City>

    suspend fun getCity(name: String, state: String, country: String): Query<City>

    suspend fun getCity(name: String, state: String): Query<City>

    suspend fun getCity(name: String): Query<City>

    suspend fun getCities(country: String): Query<City>

    suspend fun getCities(latitude: Double, longitude: Double): Query<City>

    suspend fun insertCities(cities: List<City>)

    suspend fun insertCity(
        name: String,
        latitude: Double,
        longitude: Double,
        state: String?,
        country: String?
    )

    suspend fun deleteCity(
        name: String,
        latitude: Double,
        longitude: Double
    )
}