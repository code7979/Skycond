package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.R
import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.presentation.model.City
import abhiket.skycond.presentation.model.CityWeatherMain
import abhiket.skycond.presentation.utils.ItemState
import abhiket.skycond.presentation.utils.StringValue
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.utils.asDomainCity
import abhiket.skycond.presentation.utils.asPresentationCity
import abhiket.skycond.presentation.utils.asPresentationWeather
import abhiket.skycond.presentation.utils.asStringValue
import abhiket.skycond.presentation.utils.toStringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import abhiket.skycond.domain.model.CityWeather as DomainCityWeather

class WeatherViewModel(
    private val cityWeatherRepository: CityWeatherRepository
) : ViewModel() {
    private val _cityWeatherMainState = MutableLiveData<UiState<List<CityWeatherMain>>>()
    val cityWeatherMainState: LiveData<UiState<List<CityWeatherMain>>> get() = _cityWeatherMainState

    private val _isUpdating = MutableLiveData<ItemState<CityWeatherMain>>()
    val isUpdating: LiveData<ItemState<CityWeatherMain>> get() = _isUpdating

    init {
        loadCityWeatherData()
    }

    internal fun loadCityWeatherData() {
        viewModelScope.launch(Dispatchers.Default) {
            cityWeatherRepository.getCityWeathers().onSuccess { entities ->
                val cityWeatherMainLists: List<CityWeatherMain> =
                    entities.map { domainCityWeather: DomainCityWeather ->
                        CityWeatherMain(
                            domainCityWeather.city.asPresentationCity(),
                            domainCityWeather.weather.asPresentationWeather()
                        )
                    }
                _cityWeatherMainState.postValue(UiState.Success(cityWeatherMainLists))
            }.onFailure { exception ->
                _cityWeatherMainState.postValue(UiState.Failure(exception.asStringValue()))
            }
        }
    }

    internal fun onRefresh(position: Int, city: City) {
        if (position < 0) return
        viewModelScope.launch {
            _isUpdating.postValue(ItemState.Loading(position))
            val domainCity = city.asDomainCity()
            _isUpdating.postValue(
                cityWeatherRepository.updateWeather(domainCity).fold(
                    onSuccess = {
                        cityWeatherRepository.getCityWeather(domainCity.id).fold(
                            onSuccess = { domainCityWeather ->
                                val presentationCityWeatherMain =
                                    CityWeatherMain(
                                        domainCityWeather.city.asPresentationCity(),
                                        domainCityWeather.weather.asPresentationWeather()
                                    )
                                ItemState.Success(position, presentationCityWeatherMain)
                            },
                            onFailure = {
                                ItemState.Failure(
                                    position,
                                    StringValue.StringResource(R.string.error_fetch_data)
                                )
                            }
                        )
                    },
                    onFailure = { exception ->
                        when (exception) {
                            is HttpException -> {
                                val stringRes = exception.code().toStringRes()
                                ItemState.Failure(
                                    position,
                                    StringValue.StringResource(stringRes)
                                )
                            }

                            is IOException -> {
                                ItemState.Failure(
                                    position,
                                    StringValue.StringResource(R.string.error_network_issue)
                                )
                            }

                            is KotlinNullPointerException -> {
                                ItemState.Failure(
                                    position,
                                    StringValue.StringResource(R.string.error_fetching_data)
                                )
                            }

                            else -> {
                                ItemState.Failure(
                                    position,
                                    StringValue.StringResource(R.string.error_fetch_data)
                                )
                            }
                        }
                    }
                )
            )
        }
    }
}