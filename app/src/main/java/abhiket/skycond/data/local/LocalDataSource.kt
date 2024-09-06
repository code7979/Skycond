package abhiket.skycond.data.local

import abhiket.Cities
import abhiket.CityEntity
import abhiket.WeatherDataEntity
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope

interface LocalDataSource {
    fun fetchCityEntities(): List<CityEntity>
    fun weatherDataEntityLiveData(cityId: Long, scope: CoroutineScope): LiveData<WeatherDataEntity>

    suspend fun updateWeatherDataEntity(entity: WeatherDataEntity)
    suspend fun deleteWeatherDataEntity(cityId: Long)
    suspend fun fetchWeatherDataEntity(cityId: Long): WeatherDataEntity?
    suspend fun insertWeatherDataEntity(entity: WeatherDataEntity)

    fun getAddedCityId(scope: CoroutineScope): LiveData<List<Long>>
    fun getAddedCities(scope: CoroutineScope): LiveData<List<Cities>>

}