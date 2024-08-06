package abhiket.skycond.domain

import abhiket.skycond.R
import abhiket.skycond.data.local.LocalDataSource
import abhiket.skycond.data.remote.RemoteDataSource
import abhiket.skycond.data.remote.model.City
import abhiket.skycond.uitls.UiState
import abhiket.skycond.uitls.toStringRes
import abhiket.skycond.uitls.toWeatherDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AddCityRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun getCities(query: String): UiState<List<City>> = withContext(Dispatchers.IO) {
        try {
            val cities = remoteDataSource.getCities(query)
            UiState.Success(cities)
        } catch (e: IOException) {
            UiState.Failure(R.string.network_issue)
        } catch (e: HttpException) {
            UiState.Failure(e.code().toStringRes())
        } catch (e: KotlinNullPointerException) {
            UiState.Failure(R.string.error_fetching_data)
        } catch (e: NullPointerException) {
            UiState.Failure(R.string.error_fetching_data)
        }
    }

    suspend fun addWeatherData(
        latitude: Double,
        longitude: Double
    ): UiState<Boolean> = withContext(Dispatchers.IO) {
        try {
            val weatherDataDto = remoteDataSource.getWeatherData(latitude, longitude)
            localDataSource.insertWeatherDataEntity(weatherDataDto.toWeatherDataEntity())
            UiState.Success(true)
        } catch (e: IOException) {
            UiState.Failure(R.string.network_issue)
        } catch (e: HttpException) {
            UiState.Failure(e.code().toStringRes())
        } catch (e: KotlinNullPointerException) {
            UiState.Failure(R.string.error_fetching_data)
        }
    }
}