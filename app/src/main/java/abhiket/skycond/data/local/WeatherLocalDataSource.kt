package abhiket.skycond.data.local

import abhiket.Weather

interface WeatherLocalDataSource {
    suspend fun getAllWeather(): Result<List<Weather>>
    suspend fun getWeatherByCityId(cityId: Long): Result<Weather>
    suspend fun deleteWeather(cityId: Long): Result<Boolean>
    suspend fun deleteWeathers(cityIds: List<Long>): Result<Boolean>
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
    ): Result<Boolean>

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
    ): Result<Boolean>
}