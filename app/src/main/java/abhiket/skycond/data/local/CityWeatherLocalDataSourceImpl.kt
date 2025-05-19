package abhiket.skycond.data.local
//
import abhiket.CityWeatherQueries
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class CityWeatherLocalDataSourceImpl(
    private val cityWeatherQueries: CityWeatherQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CityWeatherLocalDataSource {
    override val cityWeathers: Flow<List<abhiket.CityWeather>>
        get() = cityWeatherQueries.cityWeather().asFlow().mapToList(dispatcher)
}
