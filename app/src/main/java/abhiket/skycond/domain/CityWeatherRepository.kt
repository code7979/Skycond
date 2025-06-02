package abhiket.skycond.domain

import abhiket.skycond.domain.model.City
import abhiket.skycond.domain.model.CityWeather
import kotlinx.coroutines.flow.Flow

interface CityWeatherRepository {
    val cityWeathers: Flow<List<CityWeather>>
    suspend fun getCityWeathers(): Result<List<CityWeather>>
    suspend fun getCityWeather(cityId: Long): Result<CityWeather>
    suspend fun insertWeather(city: City): Result<Boolean>
    suspend fun updateWeather(city: City):Result<Boolean>
    suspend fun deleteWeather(cityId: Long):Result<Boolean>
    suspend fun deleteWeathers(cityIds: List<Long>): Result<Boolean>
}