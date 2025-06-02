package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.domain.GeocodingRepository
import abhiket.skycond.presentation.model.CityWeatherManage
import abhiket.skycond.presentation.utils.GTAG
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.utils.asDomainCity
import abhiket.skycond.presentation.utils.asPresentationCity
import abhiket.skycond.presentation.utils.asPresentationWeather
import abhiket.skycond.presentation.utils.asStringValue
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import abhiket.skycond.domain.model.City as DomainCity
import abhiket.skycond.domain.model.CityWeather as DomainCityWeather
import abhiket.skycond.presentation.model.City as PresentationCity

class ManagerViewModel(
    private val geocodingRepository: GeocodingRepository,
    private val cityWeatherRepository: CityWeatherRepository
) : ViewModel() {
    private val _searchedQueryState = MutableLiveData<UiState<List<PresentationCity>>>()
    val searchedQueryState: LiveData<UiState<List<PresentationCity>>> get() = _searchedQueryState

    private val _cityWeatherManageState = MutableLiveData<UiState<List<CityWeatherManage>>>()
    val cityWeatherManageState: LiveData<UiState<List<CityWeatherManage>>> get() = _cityWeatherManageState

    private val _selectedItemCount = MutableLiveData<Int>()
    val selectedItemCount: LiveData<Int> get() = _selectedItemCount

    var isReload: Boolean = false
        private set

//    init {
//        viewModelScope.launch {
//            cityWeatherRepository.getCityWeathers().onSuccess { entities ->
//                val cityWeatherManageLists: List<CityWeatherManage> =
//                    entities.map { domainCityWeather: DomainCityWeather ->
//                        CityWeatherManage(
//                            domainCityWeather.city.asPresentationCity(),
//                            domainCityWeather.weather.asPresentationWeather()
//                        )
//                    }
//                _cityWeatherManageState.postValue(UiState.Success(cityWeatherManageLists))
//            }.onFailure { exception ->
//                _cityWeatherManageState.postValue(UiState.Failure(exception.asStringValue()))
//            }
//        }
//    }

    init {
        viewModelScope.launch {
            cityWeatherRepository.cityWeathers.collect { entities ->
                val cityWeatherManageLists: List<CityWeatherManage> =
                    entities.map { domainCityWeather: DomainCityWeather ->
                        CityWeatherManage(
                            domainCityWeather.city.asPresentationCity(),
                            domainCityWeather.weather.asPresentationWeather()
                        )
                    }
                _cityWeatherManageState.postValue(UiState.Success(cityWeatherManageLists))
            }
        }
    }

    fun setSelectedItemCount(size: Int) {
        _selectedItemCount.postValue(size)
    }

    fun onSearchCity(query: String) {
        _searchedQueryState.postValue(UiState.Loading)
        if (query.isNotBlank()) {
            searchCity(query)
        }
    }

    fun onAddCity(city: PresentationCity) {
        viewModelScope.launch {
            cityWeatherRepository.insertWeather(city.asDomainCity()).onSuccess { isSuccess ->
                isReload = isSuccess
            }
        }
    }

    fun onDeleteCity(city: PresentationCity) {
        viewModelScope.launch {
            cityWeatherRepository.deleteWeather(city.id).onSuccess { isSuccess ->
                isReload = isSuccess
            }
        }
    }

    fun onDeleteCities(cityIds: List<Long>) {
        viewModelScope.launch {
            cityWeatherRepository.deleteWeathers(cityIds)
        }
    }

    fun getCityById(citiesId: List<Long>) {
        viewModelScope.launch {
            citiesId.forEach {
                cityWeatherRepository.getCityWeather(it).onSuccess { cityWeather ->
                    Log.d(GTAG, "ManagerViewModel.getCityById(): ${cityWeather.city.name}")
                }.onFailure { exception ->
                    Log.d(GTAG, "ManagerViewModel.getCityById(): ${exception.message}")
                }
            }
        }
    }

    private fun searchCity(query: String) {
        viewModelScope.launch {
            val searchedResult = geocodingRepository.getGeocodedCities(query)
            searchedResult.fold(
                onSuccess = { value: List<DomainCity> ->
                    launch(Dispatchers.IO) {
                        val cityUis = value.map { it.asPresentationCity() }
                        _searchedQueryState.postValue(UiState.Success(cityUis))
                    }
                },
                onFailure = { exception ->
                    _searchedQueryState.postValue(UiState.Failure(exception.asStringValue()))
                }
            )
        }
    }

}
