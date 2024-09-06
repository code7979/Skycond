package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.City
import abhiket.skycond.data.remote.model.WeatherDataDto
import android.os.NetworkOnMainThreadException
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import java.io.IOException

class RemoteDataSourceImpl(
    private val weatherApi: WeatherApi,
    private val apiKey: String
) : RemoteDataSource {

    @Throws(
        IOException::class,
        HttpException::class,
        KotlinNullPointerException::class,
        NetworkOnMainThreadException::class
    )
    override suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherDataDto =
        withContext(Dispatchers.IO) {
            val call = weatherApi.getWeatherData(latitude, longitude, apiKey)
            val response: Response<WeatherDataDto> = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@withContext body
                } else {
                    val invocation = call.request().tag(Invocation::class.java)!!
                    val service = invocation.service()
                    val method = invocation.method()
                    throw KotlinNullPointerException("Response from ${service.name}.${method.name} was null but response body type was declared as non-null")
                }
            } else {
                throw HttpException(response)
            }
        }

    override suspend fun getCities(query: String): List<City> {
        val call = weatherApi.getCities(q = query, apiKey = apiKey)
        val response = call.execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                val invocation = call.request().tag(Invocation::class.java)!!
                val service = invocation.service()
                val method = invocation.method()
                throw KotlinNullPointerException("Response from ${service.name}.${method.name} was null but response body type was declared as non-null")
            }
        } else {
            throw HttpException(response)
        }
    }
}