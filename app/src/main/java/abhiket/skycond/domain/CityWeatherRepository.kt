package abhiket.skycond.domain

import abhiket.skycond.domain.model.City
import abhiket.skycond.domain.model.CityWeather
import abhiket.skycond.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface CityWeatherRepository {
    val cityWeathers: Flow<List<CityWeather>>
    suspend fun getWeathers(): Result<List<Weather>>
    suspend fun getWeather(city: City): Result<CityWeather>?
    suspend fun insertWeather(city: City): Result<Boolean>
    suspend fun updateWeather(city: City):Result<Boolean>
}