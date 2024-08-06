package abhiket.skycond.domain

import abhiket.skycond.presentation.fragments.WeatherScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val uiState: StateFlow<WeatherScreenState>
    fun getCitiesIds(): Flow<List<Long>>
    suspend fun fetchWeatherData(cityId: Long)
    suspend fun addWeatherData(latitude: Double, longitude: Double)
}