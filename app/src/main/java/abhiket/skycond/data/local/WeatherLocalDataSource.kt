package abhiket.skycond.data.local

import abhiket.Weather
import app.cash.sqldelight.Query

interface WeatherLocalDataSource {
    suspend fun getAllWeather(): Query<Weather>
    suspend fun getWeatherByCityId(cityId: Long): Query<Weather>
    suspend fun deleteWeather(cityId: Long, weatherId: Long)
    suspend fun updateWeather(
        conditionId: Long,
        main: String,
        description: String,
        icon: String,
        temp: Double,
        feelsLike: Double,
        tempMin: Double,
        tempMax: Double,
        pressure: Long,
        humidity: Long,
        seaLevel: Long?,
        groundLevel: Long?,
        visibility: Long,
        windSpeed: Double,
        windDeg: Long,
        clouds: Long,
        sunrise: Long,
        sunset: Long,
        timeZone: Long,
        lastUpdate: Long,
        cityId: Long
    )

    suspend fun insertWeather(
        cityId: Long,
        conditionId: Long,
        main: String,
        description: String,
        icon: String,
        temp: Double,
        feelsLike: Double,
        tempMin: Double,
        tempMax: Double,
        pressure: Long,
        humidity: Long,
        seaLevel: Long?,
        groundLevel: Long?,
        visibility: Long,
        windSpeed: Double,
        windDeg: Long,
        clouds: Long,
        sunrise: Long,
        sunset: Long,
        timeZone: Long,
        lastUpdate: Long
    )
}