package abhiket.skycond.data

import abhiket.skycond.data.local.CityWeatherLocalDataSource
import abhiket.skycond.data.local.WeatherLocalDataSource
import abhiket.skycond.data.remote.WeatherRemoteDataSource
import abhiket.skycond.data.utils.asCityWeather
import abhiket.skycond.data.utils.asWeather
import abhiket.skycond.data.utils.wrapWithResultForRemote
import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.domain.model.City
import abhiket.skycond.domain.model.CityWeather
import abhiket.skycond.domain.model.Weather
import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityWeatherRepositoryImpl(
    private val cityWeatherLocalDataSource: CityWeatherLocalDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : CityWeatherRepository {

    override val cityWeathers: Flow<List<CityWeather>>
        get() = cityWeatherLocalDataSource.cityWeathers.map { entities ->
            entities.map { cityWeather ->
                cityWeather.asCityWeather()
            }
        }

    override suspend fun getWeathers(): Result<List<Weather>> {
        return wrapWithResultForRemote {
            val weatherQuery = weatherLocalDataSource.getAllWeather()
            weatherQuery.executeAsList().map { it.asWeather() }
        }
    }

    override suspend fun getWeather(city: City): Result<CityWeather>? {
        return null
    }

    override suspend fun insertWeather(city: City): Result<Boolean> {
        return wrapWithResultForRemote {
            weatherRemoteDataSource.getWeatherData(city.latitude, city.longitude)
        }.fold(
            onSuccess = { remoteCityWeather ->
                val remoteWeather = remoteCityWeather.weather.first()
                val remoteMain = remoteCityWeather.main
                val remoteWind = remoteCityWeather.wind
                try {
                    weatherLocalDataSource.insertWeather(
                        cityId = city.id,
                        conditionId = remoteWeather.id.toLong(),
                        main = remoteWeather.main,
                        description = remoteWeather.description,
                        icon = remoteWeather.icon,
                        temp = remoteMain.temp,
                        feelsLike = remoteMain.feelsLike,
                        tempMin = remoteMain.tempMin,
                        tempMax = remoteMain.tempMax,
                        pressure = remoteMain.pressure,
                        humidity = remoteMain.humidity,
                        seaLevel = remoteMain.seaLevel,
                        groundLevel = remoteMain.groundLevel,
                        visibility = remoteCityWeather.visibility,
                        windSpeed = remoteWind.speed,
                        windDeg = remoteWind.deg,
                        clouds = remoteCityWeather.clouds.all,
                        sunrise = remoteCityWeather.sunrise,
                        sunset = remoteCityWeather.sunset,
                        timeZone = remoteCityWeather.timezone,
                        lastUpdate = remoteCityWeather.date
                    )
                    Result.success(true)
                } catch (e: SQLiteException) {
                    Result.failure(e)
                }
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun updateWeather(city: City): Result<Boolean> {
        return wrapWithResultForRemote {
            weatherRemoteDataSource.getWeatherData(city.latitude, city.longitude)
        }.fold(
            onSuccess = { remoteCityWeather ->
                val remoteWeather = remoteCityWeather.weather.first()
                val remoteMain = remoteCityWeather.main
                val remoteWind = remoteCityWeather.wind
                try {
                    weatherLocalDataSource.updateWeather(
                        conditionId = remoteWeather.id.toLong(),
                        main = remoteWeather.main,
                        description = remoteWeather.description,
                        icon = remoteWeather.icon,
                        temp = remoteMain.temp,
                        feelsLike = remoteMain.feelsLike,
                        tempMin = remoteMain.tempMin,
                        tempMax = remoteMain.tempMax,
                        pressure = remoteMain.pressure,
                        humidity = remoteMain.humidity,
                        seaLevel = remoteMain.seaLevel,
                        groundLevel = remoteMain.groundLevel,
                        visibility = remoteCityWeather.visibility,
                        windSpeed = remoteWind.speed,
                        windDeg = remoteWind.deg,
                        clouds = remoteCityWeather.clouds.all,
                        sunrise = remoteCityWeather.sunrise,
                        sunset = remoteCityWeather.sunset,
                        timeZone = remoteCityWeather.timezone,
                        lastUpdate = remoteCityWeather.date,
                        cityId = city.id
                    )
                    Result.success(true)
                } catch (e: SQLiteException) {
                    Result.failure(e)
                }
            },
            onFailure = { Result.failure(it) }
        )
    }
}