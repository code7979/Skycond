package abhiket.skycond.presentation.fragments

import abhiket.skycond.domain.Repository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: Repository) : ViewModel() {
    val uiState: StateFlow<WeatherScreenState> get() = repository.uiState
    fun getWeatherData(id: Long) {
        viewModelScope.launch {
            repository.fetchWeatherData(id)
        }
    }

    companion object {
        private const val TAG = "WeatherScreenFragment"
    }
}
