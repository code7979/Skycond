package abhiket.skycond.data.local

import abhiket.CityWeather
import kotlinx.coroutines.flow.Flow

interface CityWeatherLocalDataSource {
    val cityWeathers: Flow<List<CityWeather>>
}