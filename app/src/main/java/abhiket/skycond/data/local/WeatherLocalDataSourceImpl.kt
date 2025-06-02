package abhiket.skycond.data.local

import abhiket.Weather
import abhiket.WeatherQueries
import android.database.sqlite.SQLiteException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherLocalDataSourceImpl(
    private val weatherQueries: WeatherQueries
) : WeatherLocalDataSource {

    override suspend fun getAllWeather(): Result<List<Weather>> {
        return withContext(Dispatchers.IO) {
            try {
                val weathers = weatherQueries.getAllWeather().executeAsList()
                Result.success(weathers)
            } catch (exception: SQLiteException) {
                Result.failure(exception)
            }
        }
    }

    override suspend fun getWeatherByCityId(cityId: Long): Result<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val weather = weatherQueries.getWeatherByCityId(cityId).executeAsOne()
                Result.success(weather)
            } catch (exception: NullPointerException) {
                Result.failure(exception)
            } catch (exception: SQLiteException) {
                Result.failure(exception)
            }
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
        try {
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
            Result.success(true)
        } catch (exception: SQLiteException) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteWeather(cityId: Long): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                weatherQueries.deleteWeather(cityId)
                Result.success(true)
            } catch (exception: SQLiteException) {
                Result.failure(exception)
            }
        }
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
        try {
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
            Result.success(true)
        } catch (exception: SQLiteException) {
            Result.failure(exception)
        }
    }

}