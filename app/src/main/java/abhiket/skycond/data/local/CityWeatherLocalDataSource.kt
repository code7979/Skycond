package abhiket.skycond.data.local

import abhiket.CityWeather
import abhiket.GetCityWeatherById
import kotlinx.coroutines.flow.Flow

interface CityWeatherLocalDataSource {
    val cityWeathers: Flow<List<CityWeather>>
    suspend fun getCityWeathers(): Result<List<CityWeather>>
    suspend fun getCityWeather(cityId: Long):Result<GetCityWeatherById>
}