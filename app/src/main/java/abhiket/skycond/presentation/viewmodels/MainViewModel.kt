package abhiket.skycond.presentation.viewmodels

import abhiket.skycond.data.local.LocalDataSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras

class MainViewModel(private val repository: LocalDataSource) : ViewModel() {
    val citiesIds: LiveData<List<Long>> get() = repository.getAddedCityId(viewModelScope)

    companion object {
        fun createFactory(repository: LocalDataSource): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainViewModel(repository) as T
                }
            }
    }
}
