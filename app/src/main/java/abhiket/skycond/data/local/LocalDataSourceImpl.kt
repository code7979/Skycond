package abhiket.skycond.data.local

import abhiket.CityEntity
import abhiket.WeatherDataEntity
import abhiket.WeatherDataQueries
import abhiket.skycond.uitls.extensions.asFlow
import abhiket.skycond.uitls.extensions.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val queries: WeatherDataQueries) : LocalDataSource {

    override fun fetchCityEntitiesIds(): Flow<List<Long>> {
        return queries.getCitiesId().asFlow().mapToList(Dispatchers.IO)
    }

    override fun fetchCityEntities(): List<CityEntity> {
        return queries.getCities().executeAsList()
    }

    override fun insertWeatherDataEntity(entity: WeatherDataEntity) {
        try {
            queries.insertWeatherData(
                cityId = entity.cityId,
                cityName = entity.cityName,
                cityCountry = entity.cityCountry,
                cityLatitude = entity.cityLatitude,
                cityLongitude = entity.cityLongitude,
                lastUpdate = entity.lastUpdate,
                timeZone = entity.timeZone,
                description = entity.description,
                icon = entity.icon,
                temp = entity.temp,
                feelsLike = entity.feelsLike,
                tempMin = entity.tempMin,
                tempMax = entity.tempMax,
                pressure = entity.pressure,
                humidity = entity.humidity,
                seaLevel = entity.seaLevel,
                groundLevel = entity.groundLevel,
                visibility = entity.visibility,
                windSpeed = entity.windSpeed,
                windDeg = entity.windDeg,
                clouds = entity.clouds,
                sunrise = entity.sunrise,
                sunset = entity.sunset
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun updateWeatherDataEntity(entity: WeatherDataEntity) {
        try {
            queries.updateWeatherDataEntity(
                cityId = entity.cityId,
                lastUpdate = entity.lastUpdate,
                timeZone = entity.timeZone,
                description = entity.description,
                icon = entity.icon,
                temp = entity.temp,
                feelsLike = entity.feelsLike,
                tempMin = entity.tempMin,
                tempMax = entity.tempMax,
                pressure = entity.pressure,
                humidity = entity.humidity,
                seaLevel = entity.seaLevel,
                groundLevel = entity.groundLevel,
                visibility = entity.visibility,
                windSpeed = entity.windSpeed,
                windDeg = entity.windDeg,
                clouds = entity.clouds,
                sunrise = entity.sunrise,
                sunset = entity.sunset
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteWeatherDataEntity(cityId: Long) = queries.deleteWeatherData(cityId)

    override fun fetchWeatherDataEntity(cityId: Long): WeatherDataEntity? =
        try {
            queries.weatherDataEntity(cityId).executeAsOne()
        } catch (e: IllegalStateException) {
            null
        } catch (e: NullPointerException) {
            null
        }
}