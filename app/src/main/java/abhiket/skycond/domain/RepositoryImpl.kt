package abhiket.skycond.domain

import abhiket.skycond.R
import abhiket.skycond.data.local.LocalDataSource
import abhiket.skycond.data.remote.RemoteDataSource
import abhiket.skycond.presentation.fragments.WeatherScreenState
import abhiket.skycond.uitls.shouldUpdateData
import abhiket.skycond.uitls.toStringRes
import abhiket.skycond.uitls.toWeatherDataEntity
import abhiket.skycond.uitls.toWeatherDataUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    private val _uiState = MutableStateFlow(WeatherScreenState())
    override val uiState: StateFlow<WeatherScreenState> get() = _uiState.asStateFlow()

    override fun getCitiesIds(): Flow<List<Long>> {
        return localDataSource.fetchCityEntitiesIds()
    }

    private suspend fun updateWeatherDataState(
        latitude: Double, longitude: Double
    ) = _uiState.update { state ->
        try {
            val dto = remoteDataSource.getWeatherData(latitude, longitude)
            localDataSource.updateWeatherDataEntity(dto.toWeatherDataEntity())
            state.copy(weatherDataUi = dto.toWeatherDataUi(), isLoading = false)
        } catch (e: IOException) {
            state.copy(message = R.string.network_issue, isLoading = false)
        } catch (e: HttpException) {
            state.copy(message = e.code().toStringRes(), isLoading = false)
        } catch (e: KotlinNullPointerException) {
            state.copy(message = R.string.error_fetching_data, isLoading = false)
        }
    }

    override suspend fun fetchWeatherData(cityId: Long) = withContext(Dispatchers.IO) {
        val entity = localDataSource.fetchWeatherDataEntity(cityId) ?: return@withContext
        _uiState.update { state ->
            state.copy(
                weatherDataUi = entity.toWeatherDataUi(), isLoading = false
            )
        }

        if (shouldUpdateData(entity.lastUpdate)) {
            _uiState.update { state -> state.copy(isLoading = true) }
            updateWeatherDataState(entity.cityLatitude, entity.cityLongitude)
        }
    }

    override suspend fun addWeatherData(
        latitude: Double,
        longitude: Double
    ) = withContext(Dispatchers.IO) {
        try {
            val dto = remoteDataSource.getWeatherData(latitude, longitude)
            localDataSource.insertWeatherDataEntity(dto.toWeatherDataEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}