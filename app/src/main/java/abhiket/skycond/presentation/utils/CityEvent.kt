package abhiket.skycond.presentation.utils

import java.io.Serializable

sealed class CityEvent(val cityId: Long) : Serializable {
    class Add(cityId: Long) : CityEvent(cityId)
    class Remove(cityId: Long) : CityEvent(cityId)
}