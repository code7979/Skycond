package abhiket.skycond.data.local

import abhiket.Weather
import abhiket.WeatherQueries
import app.cash.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherLocalDataSourceImpl(
    private val weatherQueries: WeatherQueries
) : WeatherLocalDataSource {

    override suspend fun getAllWeather(): Query<Weather> {
        return withContext(Dispatchers.IO) {
            weatherQueries.getAllWeather()
        }
    }

    override suspend fun getWeatherByCityId(cityId: Long): Query<Weather> {
        return withContext(Dispatchers.IO) {
            weatherQueries.getWeatherByCityId(cityId)
        }
    }

    override suspend fun insertWeather(
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
    ) = withContext(Dispatchers.IO) {
        weatherQueries.insertWeather(
            cityId,
            conditionId,
            main,
            description,
            icon,
            temp,
            feelsLike,
            tempMin,
            tempMax,
            pressure,
            humidity,
            seaLevel,
            groundLevel,
            visibility,
            windSpeed,
            windDeg,
            clouds,
            sunrise,
            sunset,
            timeZone,
            lastUpdate
        )
    }

    override suspend fun deleteWeather(cityId: Long, weatherId: Long) =
        withContext(Dispatchers.IO) {
            weatherQueries.deleteWeather(cityId, weatherId)
        }

    override suspend fun updateWeather(
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
    ) = withContext(Dispatchers.IO) {
        weatherQueries.updateWeather(
            conditionId,
            main,
            description,
            icon,
            temp,
            feelsLike,
            tempMin,
            tempMax,
            pressure,
            humidity,
            seaLevel,
            groundLevel,
            visibility,
            windSpeed,
            windDeg,
            clouds,
            sunrise,
            sunset,
            timeZone,
            lastUpdate,
            cityId
        )
    }

}