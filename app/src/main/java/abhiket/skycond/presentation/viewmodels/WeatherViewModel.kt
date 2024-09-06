package abhiket.skycond.presentation.viewmodels

import abhiket.WeatherDataEntity
import abhiket.skycond.R
import abhiket.skycond.data.local.LocalDataSource
import abhiket.skycond.data.remote.RemoteDataSource
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.fragments.WeatherFragment
import abhiket.skycond.uitls.ScreenState
import abhiket.skycond.uitls.shouldUpdateData
import abhiket.skycond.uitls.toStringRes
import abhiket.skycond.uitls.toWeatherDataEntity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _isLoading = MutableLiveData<ScreenState>()
    val isLoading: LiveData<ScreenState> get() = _isLoading

    private val cityId = savedStateHandle.get<Long>(WeatherFragment.ARG_CITY_ID) ?: -1
    val weatherDataEntity: LiveData<WeatherDataEntity> =
        localDataSource.weatherDataEntityLiveData(cityId, viewModelScope)

    fun updateWeatherData() {
        val data = weatherDataEntity.value
        if (data != null && shouldUpdateData(data.lastUpdate)) {
            updateWeatherDataUsingNetwork(data)
        }
    }

    private fun updateWeatherDataUsingNetwork(entity: WeatherDataEntity) = viewModelScope.launch {
        // Set start loading (Display progress indicator)
        _isLoading.postValue(ScreenState.Loading)
        val longitude = entity.cityLongitude
        val latitude = entity.cityLatitude
        try {
            val weatherData = remoteDataSource.getWeatherData(latitude, longitude)
            localDataSource.updateWeatherDataEntity(weatherData.toWeatherDataEntity())
            _isLoading.postValue(ScreenState.Success)
        } catch (e: IOException) {
            _isLoading.postValue(ScreenState.Failure(R.string.network_issue))
        } catch (e: HttpException) {
            _isLoading.postValue(ScreenState.Failure(e.code().toStringRes()))
        } catch (e: KotlinNullPointerException) {
            _isLoading.postValue(ScreenState.Failure(R.string.error_fetching_data))
        } catch (e: NullPointerException) {
            _isLoading.postValue(ScreenState.Failure(R.string.error_fetching_data))
        }
    }

    companion object {
        private const val TAG = "WeatherFragment"
        fun createViewModelFactory(context: Context) = viewModelFactory {
            initializer {
                val appModule = Singleton.getInstance(context).appModule
                WeatherViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    remoteDataSource = appModule.remoteDataSource,
                    localDataSource = appModule.localDataSource
                )
            }
        }
    }

}
