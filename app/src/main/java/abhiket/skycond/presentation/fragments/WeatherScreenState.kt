package abhiket.skycond.presentation.fragments

import abhiket.skycond.presentation.model.WeatherDataUi

data class WeatherScreenState(
    val weatherDataUi: WeatherDataUi? = null,
    val isLoading: Boolean = false,
    val message: Int? = null
)