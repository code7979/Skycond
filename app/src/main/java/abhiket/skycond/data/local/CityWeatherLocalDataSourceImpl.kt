package abhiket.skycond.data.local
//
import abhiket.CityWeather
import abhiket.CityWeatherQueries
import abhiket.GetCityWeatherById
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CityWeatherLocalDataSourceImpl(
    private val cityWeatherQueries: CityWeatherQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CityWeatherLocalDataSource {
    override val cityWeathers: Flow<List<abhiket.CityWeather>>
        get() = cityWeatherQueries.cityWeather().asFlow().mapToList(dispatcher)

    override suspend fun getCityWeathers(): Result<List<CityWeather>> {
        return withContext(dispatcher) {
            try {
                Result.success(cityWeatherQueries.cityWeather().executeAsList())
            } catch (exception: SQLiteException) {
                Result.failure(exception)
            }
        }
    }

    override suspend fun getCityWeather(cityId: Long): Result<GetCityWeatherById> {
        return withContext(dispatcher) {
            try {
                val getCityWeather = cityWeatherQueries.getCityWeatherById(cityId).executeAsOne()
                Result.success(getCityWeather)
            } catch (exception: SQLiteException) {
                Result.failure(exception)
            }
        }
    }
}
