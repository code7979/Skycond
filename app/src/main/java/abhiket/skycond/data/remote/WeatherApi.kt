package abhiket.skycond.data.remote

import abhiket.skycond.data.remote.model.City
import abhiket.skycond.data.remote.model.WeatherDataDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }

    @GET("/data/2.5/weather")
    fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<WeatherDataDto>

    @GET("/geo/1.0/direct")
    fun getCities(
        @Query("q") q: String,
        @Query("limit") limit: Int = 20,
        @Query("appid") apiKey: String
    ): Call<List<City>>
}