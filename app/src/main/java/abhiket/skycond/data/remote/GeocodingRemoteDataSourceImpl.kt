package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.City
import android.os.NetworkOnMainThreadException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Invocation
import java.io.IOException

class GeocodingRemoteDataSourceImpl(
    private val geocodingApi: GeocodingApi,
    private val apiKey: String
) : GeocodingRemoteDataSource {

    @Throws(
        IOException::class,
        HttpException::class,
        KotlinNullPointerException::class,
        NetworkOnMainThreadException::class
    )
    override suspend fun getCities(query: String): List<City> {
        return withContext(Dispatchers.IO) {
            val call = geocodingApi.getCities(q = query, apiKey = apiKey)
            val response = call.execute()
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
    }
}