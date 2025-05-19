package abhiket.skycond.domain

import abhiket.skycond.domain.model.City


interface GeocodingRepository {
    suspend fun getGeocodedCities(query: String): Result<List<City>>
}