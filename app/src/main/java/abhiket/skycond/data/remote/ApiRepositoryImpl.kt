package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.WeatherDataDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import java.io.IOException

class ApiRepositoryImpl(private val weatherApi: WeatherApi, private val apiKey: String) {

    suspend fun getWeatherData(latitude: Double, longitude: Double) = withContext(Dispatchers.IO) {
        val call = weatherApi.getWeatherData(latitude, longitude, apiKey)
        try {
            val response: Response<WeatherDataDto> = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    val invocation = call.request().tag(Invocation::class.java)!!
                    val service = invocation.service()
                    val method = invocation.method()
                    val exception =
                        KotlinNullPointerException("Response from ${service.name}.${method.name} was null but response body type was declared as non-null")
                    Result.failure(exception)
                }
            } else {
                Result.failure(HttpException(response))
            }
        } catch (exception: IOException) {
            Result.failure(exception)
        }
    }
}

//        call.enqueue(object : Callback<WeatherData> {
//
//            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body == null) {
//                        val invocation = call.request().tag(Invocation::class.java)!!
//                        val service = invocation.service()
//                        val method = invocation.method()
//                        val e = KotlinNullPointerException(
//                            "Response from ${service.name}.${method.name}" +
//                                    " was null but response body type was declared as non-null",
//                        )
//                        onComplete(Result.failure(e))
//                    } else {
//                        onComplete(Result.success(body))
//                    }
//                } else {
//                    onComplete(Result.failure(HttpException(response)))
//                }
//
//            }
//
//            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                onComplete(Result.failure(t))
//                Log.d(
//                    GLOBAL_TAG,
//                    "ApiRepositoryImpl:- onFailure: ${Thread.currentThread().name}"
//                )
//            }
//
//        })