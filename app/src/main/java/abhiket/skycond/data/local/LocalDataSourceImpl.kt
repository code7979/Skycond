package abhiket.skycond.data.local

import abhiket.Cities
import abhiket.CityEntity
import abhiket.WeatherDataEntity
import abhiket.WeatherDataQueries
import abhiket.skycond.data.utils.asListLiveData
import abhiket.skycond.data.utils.asLiveData
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(private val queries: WeatherDataQueries) : LocalDataSource {

    override fun fetchCityEntities(): List<CityEntity> {
        return queries.getCities().executeAsList()
    }

    override suspend fun insertWeatherDataEntity(entity: WeatherDataEntity) {
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

    override suspend fun updateWeatherDataEntity(entity: WeatherDataEntity) {
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
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteWeatherDataEntity(cityId: Long) = queries.deleteWeatherData(cityId)

    override suspend fun fetchWeatherDataEntity(
        cityId: Long
    ): WeatherDataEntity? = withContext(Dispatchers.IO) {
        try {
            queries.weatherDataEntity(cityId).executeAsOne()
        } catch (e: IllegalStateException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    override fun weatherDataEntityLiveData(
        cityId: Long,
        scope: CoroutineScope
    ): LiveData<WeatherDataEntity> {
        return queries.weatherDataEntity(cityId).asLiveData(scope)
    }

    override fun getAddedCities(scope: CoroutineScope): LiveData<List<Cities>> {
        return queries.Cities().asListLiveData(scope)
    }

    override fun getAddedCityId(scope: CoroutineScope): LiveData<List<Long>> {
        return queries.getCitiesId().asListLiveData(scope)
    }
}