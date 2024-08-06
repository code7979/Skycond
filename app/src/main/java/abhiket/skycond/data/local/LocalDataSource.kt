package abhiket.skycond.data.local

import abhiket.CityEntity
import abhiket.WeatherDataEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun fetchCityEntitiesIds(): Flow<List<Long>>
    fun fetchCityEntities():List<CityEntity>

    fun updateWeatherDataEntity(entity: WeatherDataEntity)
    fun deleteWeatherDataEntity(cityId: Long)
    fun fetchWeatherDataEntity(cityId: Long): WeatherDataEntity?
    fun insertWeatherDataEntity(entity: WeatherDataEntity)

}