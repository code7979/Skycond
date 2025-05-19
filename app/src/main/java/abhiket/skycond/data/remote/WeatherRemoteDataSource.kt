package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.CityWeather
import retrofit2.HttpException
import java.io.IOException

interface WeatherRemoteDataSource {
    @Throws(IOException::class, HttpException::class, KotlinNullPointerException::class)
    suspend fun getWeatherData(latitude: Double, longitude: Double): CityWeather
}