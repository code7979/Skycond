package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.CityWeather
import android.os.NetworkOnMainThreadException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import java.io.IOException

class WeatherRemoteDataSourceImpl(
    private val weatherApi: WeatherApi,
    private val apiKey: String
) : WeatherRemoteDataSource {

    @Throws(
        IOException::class,
        HttpException::class,
        KotlinNullPointerException::class,
        NetworkOnMainThreadException::class
    )
    override suspend fun getWeatherData(latitude: Double, longitude: Double): CityWeather {
        return withContext(Dispatchers.IO) {
            val call = weatherApi.getWeatherData(latitude, longitude, apiKey)
            val response: Response<CityWeather> = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@withContext body
                } else {
                    val invocation = call.request().tag(Invocation::class.java)!!
                    val service = invocation.service()
                    val method = invocation.method()
                    throw KotlinNullPointerException(
                        "Response from ${service.name}.${method.name} was null but response body type was declared as non-null"
                    )
                }
            } else {
                throw HttpException(response)
            }
        }
    }

}