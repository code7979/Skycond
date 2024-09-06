package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.City
import abhiket.skycond.data.remote.model.WeatherDataDto
import retrofit2.HttpException
import java.io.IOException

interface RemoteDataSource {
    @Throws(IOException::class, HttpException::class, KotlinNullPointerException::class)
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
    ): WeatherDataDto

    @Throws(IOException::class, HttpException::class, KotlinNullPointerException::class)
    suspend fun getCities(query: String): List<City>
}