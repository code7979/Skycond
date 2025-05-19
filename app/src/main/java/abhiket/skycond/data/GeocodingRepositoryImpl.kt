package abhiket.skycond.data

import abhiket.skycond.data.local.GeocodingLocalDataSource
import abhiket.skycond.data.remote.GeocodingRemoteDataSource
import abhiket.skycond.data.utils.asCity
import abhiket.skycond.data.utils.toLocationParts
import abhiket.skycond.domain.GeocodingRepository
import abhiket.skycond.domain.model.City
import app.cash.sqldelight.Query
import retrofit2.HttpException
import java.io.IOException

class GeocodingRepositoryImpl(
    private val geocodingLocalDataSource: GeocodingLocalDataSource,
    private val geocodingRemoteDataSource: GeocodingRemoteDataSource
) : GeocodingRepository {

    /**
     * Checks whether the requested city exists in the local database.
     * If found, returns the cached result.
     * Otherwise, fetches the city data from the server, stores it in the database, and returns it.
     *
     * @param query city name
     * @return list of city
     */
    override suspend fun getGeocodedCities(query: String): Result<List<City>> {
        return localGeocodedCitiesQuery(query).fold(
            onSuccess = { localCitiesQuery: Query<abhiket.City> ->
                val localCities = localCitiesQuery.executeAsList()
                if (localCities.isNotEmpty()) {
                    Result.success(localCities.map { it.asCity() })
                } else {
                    // Here, `remoteDataSource.getCities(query)` fetches data from the server,
                    remoteGeocodedCities(query).fold(
                        onSuccess = { remoteCities ->
                            // In the forEach block, we insert the data into the local database.
                            remoteCities.forEach { remoteCity ->
                                geocodingLocalDataSource.insertCity(
                                    name = remoteCity.name,
                                    state = remoteCity.state,
                                    country = remoteCity.country,
                                    latitude = remoteCity.latitude,
                                    longitude = remoteCity.longitude,
                                )
                            }
                            // Then we return back to `map` to transform the data into `City` objects.
                            Result.success(localCitiesQuery.executeAsList().map { it.asCity() })
                        },
                        onFailure = {
                            Result.failure(it)
                        }
                    )
                }
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private suspend fun localGeocodedCitiesQuery(query: String): Result<Query<abhiket.City>> {
        val parts = query.toLocationParts()
        return when (parts.size) {
            1 -> Result.success(geocodingLocalDataSource.getCity(parts[0]))
            2 -> Result.success(geocodingLocalDataSource.getCity(parts[0], parts[1]))
            3 -> {
                if (parts[2].length == 2) {
                    Result.success(geocodingLocalDataSource.getCity(parts[0], parts[1], parts[2]))
                } else {
                    Result.success(geocodingLocalDataSource.getCity(parts[0], parts[1]))
                }
            }

            0 -> Result.failure(MalformedException("At least one part is required for searching cities."))
            else -> Result.failure(MalformedException("Too many parts in location. Expected format: City, State, CountryCode"))
        }
    }

    // Here, remoteGeocodedCities means that it will be fetched from the remote database(server).
    private suspend fun remoteGeocodedCities(
        query: String
    ): Result<List<abhiket.skycond.data.remote.model.City>> {
        return try {
            Result.success(geocodingRemoteDataSource.getCities(query))
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: KotlinNullPointerException) {
            Result.failure(e)
        }
    }

    /*
    fun extractLocationParts(input: String) {
        val regex = Regex(pattern = """^([A-Za-z\s]+)\s*,+\s*([A-Za-z\s]+)\s*,+\s*([A-Za-z]{2})$""")
        val matchResult = regex.find(input)
        matchResult?.let {
            val (city, state, country) = it.destructured
            println(city)
            println(state)
            println(country)
        }
    }
     */


}