package abhiket.skycond.data.utils

sealed interface Location {
    data class City(val city: String) : Location
    data class State(val city: String, val state: String) : Location
    data class Country(val city: String, val state: String, val country: String) : Location
}