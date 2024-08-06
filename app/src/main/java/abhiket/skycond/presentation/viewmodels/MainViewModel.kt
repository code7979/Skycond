package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.domain.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val citiesIds: LiveData<List<Long>> get() = repository.getCitiesIds().asLiveData()

    companion object {
        fun createFactory(repository: Repository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainViewModel(repository) as T
                }
            }

        const val LONGITUDE: Double = 85.1376
        const val LATITUDE: Double = 25.5941
    }

    fun addDummyCity() {
        viewModelScope.launch {
            repository.addWeatherData(latitude = LATITUDE, longitude = LONGITUDE)
        }
    }
}
