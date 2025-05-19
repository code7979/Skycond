package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.R
import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.presentation.model.City
import abhiket.skycond.presentation.utils.ItemState
import abhiket.skycond.presentation.utils.StringValue
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.utils.asDomainCity
import abhiket.skycond.presentation.utils.asPresentationCity
import abhiket.skycond.presentation.utils.asPresentationWeather
import abhiket.skycond.presentation.utils.toStringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import abhiket.skycond.domain.model.CityWeather as DomainCityWeather
import abhiket.skycond.presentation.model.CityWeather as PresentationCityWeather

class WeatherViewModel(
    private val cityWeatherRepository: CityWeatherRepository
) : ViewModel() {
    private val _cityWeatherState = MutableLiveData<UiState<List<PresentationCityWeather>>>()
    val cityWeatherState: LiveData<UiState<List<PresentationCityWeather>>> get() = _cityWeatherState

    private val _isUpdating = MutableLiveData<ItemState<Int>>()
    val isUpdating: LiveData<ItemState<Int>> get() = _isUpdating


    init {
        viewModelScope.launch {
            _cityWeatherState.value = UiState.Loading
            cityWeatherRepository.cityWeathers.collect { entities ->
                val cityWeatherList: List<PresentationCityWeather> =
                    entities.map { domainCityWeather: DomainCityWeather ->
                        PresentationCityWeather(
                            domainCityWeather.city.asPresentationCity(),
                            domainCityWeather.weather.asPresentationWeather()
                        )
                    }
                _cityWeatherState.value = UiState.Success(cityWeatherList)
            }
        }
    }

    fun onRefresh(position: Int, city: City) = viewModelScope.launch {
        _isUpdating.postValue(ItemState.Loading(position))
        cityWeatherRepository.updateWeather(city.asDomainCity()).fold(
            onSuccess = {
                _isUpdating.postValue(ItemState.Success(position))
            },
            onFailure = { exception ->
                when (exception) {
                    is HttpException -> {
                        val stringRes = exception.code().toStringRes()
                        _isUpdating.postValue(
                            ItemState.Failure(position, StringValue.StringResource(stringRes))
                        )
                    }

                    is IOException -> {
                        _isUpdating.postValue(
                            ItemState.Failure(
                                position,
                                StringValue.StringResource(R.string.error_network_issue)
                            )
                        )
                    }

                    is KotlinNullPointerException -> {
                        _isUpdating.postValue(
                            ItemState.Failure(
                                position,
                                StringValue.StringResource(R.string.error_fetching_data)
                            )
                        )
                    }

                    else -> {
                        _isUpdating.postValue(
                            ItemState.Failure(
                                position,
                                StringValue.StringResource(R.string.error_fetch_data)
                            )
                        )
                    }
                }
            }
        )
    }

}