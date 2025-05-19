package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.City
import retrofit2.HttpException
import java.io.IOException

interface GeocodingRemoteDataSource {
    @Throws(IOException::class, HttpException::class, KotlinNullPointerException::class)
    suspend fun getCities(query: String): List<City>
}