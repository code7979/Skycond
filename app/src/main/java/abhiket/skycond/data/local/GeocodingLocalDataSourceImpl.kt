package abhiket.skycond.data.local

import abhiket.City
import abhiket.CityQueries
import app.cash.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeocodingLocalDataSourceImpl(
    private val cityQueries: CityQueries
) : GeocodingLocalDataSource {

    override suspend fun getCities(): Query<City> = withContext(Dispatchers.IO) {
       cityQueries.getCities()
    }

    override suspend fun getCity(
        name: String,
        state: String,
        country: String
    ): Query<City> = withContext(Dispatchers.IO) {
        cityQueries.getCity(name, state, country)
    }

    override suspend fun getCity(
        name: String,
        state: String
    ): Query<City> = withContext(Dispatchers.IO) {
        cityQueries.getCityByState(name, state)
    }

    override suspend fun getCity(
        name: String
    ): Query<City> = withContext(Dispatchers.IO) {
        cityQueries.getCityByName(name)
    }

    override suspend fun getCities(country: String): Query<City> = withContext(Dispatchers.IO) {
        cityQueries.getCityByCountry(country)
    }

    override suspend fun getCities(
        latitude: Double,
        longitude: Double
    ): Query<City> = withContext(Dispatchers.IO) {
        cityQueries.getCityCoordinate(latitude, longitude)
    }

    override suspend fun insertCity(
        name: String,
        latitude: Double,
        longitude: Double,
        state: String?,
        country: String?
    ) = withContext(Dispatchers.IO) {
        cityQueries.insertCity(name, latitude, longitude, state, country)
    }

    override suspend fun insertCities(cities: List<City>) {
        cityQueries.transaction {
            cities.forEach { city: City ->
                cityQueries.insertCity(
                    name = city.name,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    state = city.state,
                    country = city.country
                )
            }
        }
    }

    suspend fun insertCitiesNew() {
        cityQueries.transaction {  }
    }

    override suspend fun deleteCity(
        name: String,
        latitude: Double,
        longitude: Double
    ) = withContext(Dispatchers.IO) {
        cityQueries.deleteCity(name, latitude, longitude)
    }

}