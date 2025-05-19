package abhiket.skycond.presentation.model

import java.util.Locale

data class City(
    val id: Long,
    val name: String,
    val state: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
) {

    fun getFormattedCity(): String {
        val formattedState = String.format(Locale.getDefault(), "%s, %s, %s", name, state, country)
        return formattedState
    }

    fun getFormattedStateAndCountry(): String {
        val formattedState = String.format(Locale.getDefault(), "%s, %s", state, country)
        return formattedState
    }
}
