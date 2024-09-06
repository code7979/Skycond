package abhiket.skycond.presentation.viewmodels

import abhiket.Cities
import abhiket.skycond.data.remote.model.City
import abhiket.skycond.domain.AddCityRepository
import abhiket.skycond.uitls.UiState
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class AddViewModel private constructor(private val repository: AddCityRepository) : ViewModel() {
    private val _locations = MutableLiveData<UiState<List<City>>>()
    val locations: LiveData<UiState<List<City>>> get() = _locations

    private val _isAdded = MutableLiveData<UiState<Boolean>>()
    val isAdded: LiveData<UiState<Boolean>> get() = _isAdded

    val cities: LiveData<List<Cities>> get() = repository.getAddedCityList(viewModelScope)

    fun onQuery(query: String) {
        viewModelScope.launch {
            _locations.value = UiState.Loading
            _locations.value = repository.getCities(query)
        }
    }

    fun onAddCity(city: City) {
        viewModelScope.launch {
            val isAdded = repository.addWeatherData(city.lat, city.lon)
            _isAdded.value = isAdded
        }
    }

    fun removeCityById(cityId: Long) {
        viewModelScope.launch {
            try {
                repository.removeCityById(cityId)
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
            }

        }
    }

    companion object {
        fun createFactory(repository: AddCityRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return AddViewModel(repository) as T
                }
            }

        const val TAG = "AddViewModel"
    }

}
