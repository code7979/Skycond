package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.R
import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.domain.GeocodingRepository
import abhiket.skycond.presentation.utils.StringValue
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.utils.asDomainCity
import abhiket.skycond.presentation.utils.asPresentationCity
import abhiket.skycond.presentation.utils.toStringRes
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import abhiket.skycond.domain.model.City as DomainCity

class ManagerViewModel(
    private val geocodingRepository: GeocodingRepository,
    private val weatherRepository: CityWeatherRepository
) : ViewModel() {
    private val _searchedQueryState =
        MutableLiveData<UiState<List<abhiket.skycond.presentation.model.City>>>()
    val searchedQueryState: LiveData<UiState<List<abhiket.skycond.presentation.model.City>>> get() = _searchedQueryState

    fun onSearchCity(query: String) {
        _searchedQueryState.postValue(UiState.Loading)
        if (query.isNotBlank()) {
            searchCity(query)
        }
    }

    fun onAddCity(city: abhiket.skycond.presentation.model.City) = viewModelScope.launch {
        weatherRepository.insertWeather(city.asDomainCity())
    }

    private fun searchCity(query: String) = viewModelScope.launch {
        val searchedResult = geocodingRepository.getGeocodedCities(query)
        searchedResult.fold(
            onSuccess = { value: List<DomainCity> ->
                launch(Dispatchers.IO) {
                    val cityUis = value.map { it.asPresentationCity() }
                    _searchedQueryState.postValue(UiState.Success(cityUis))
                }
            },
            onFailure = { exception ->
                when (exception) {
                    is HttpException -> {
                        val stringRes = exception.code().toStringRes()
                        _searchedQueryState.postValue(
                            UiState.Failure(
                                StringValue.StringResource(
                                    stringRes
                                )
                            )
                        )
                    }

                    is IOException -> {
                        _searchedQueryState.postValue(UiState.Failure(StringValue.StringResource(R.string.error_network_issue)))
                    }

                    is KotlinNullPointerException -> {
                        _searchedQueryState.postValue(UiState.Failure(StringValue.StringResource(R.string.error_fetching_data)))
                    }

                    else -> {
                        _searchedQueryState.postValue(
                            UiState.Failure(
                                StringValue.DynamicString(
                                    exception.javaClass.name
                                )
                            )
                        )
                    }
                }
            }
        )
    }


}
