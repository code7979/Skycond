@file:JvmName("Mapper")

package abhiket.skycond.presentation.utils

import abhiket.skycond.R
import androidx.annotation.DrawableRes
import abhiket.skycond.domain.model.City as DomainCity
import abhiket.skycond.domain.model.Weather as DomainWeather
import abhiket.skycond.presentation.model.City as PresentationCity
import abhiket.skycond.presentation.model.Weather as PresentationWeather

const val EMPTY_STRING = ""
const val CACHE_EXPIRY_TIME_IN_SEC = 600 //Second ( 10 minutes )
const val GTAG = "SkycondWeatherApp"

@JvmName("toCityUi")
fun DomainCity.asPresentationCity(): PresentationCity {
    return PresentationCity(
        id = this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        state = this.state ?: EMPTY_STRING,
        country = this.country ?: EMPTY_STRING
    )
}

@JvmName("toCity")
fun PresentationCity.asDomainCity(): DomainCity {
    return DomainCity(
        id = this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        state = this.state,
        country = this.country
    )
}

@JvmName("toCityWeather")
fun DomainWeather.asPresentationWeather(): PresentationWeather {
    return PresentationWeather(
        conditionId,
        main,
        description,
        icon,
        temp,
        feelsLike,
        tempMin,
        tempMax,
        pressure,
        humidity,
        seaLevel,
        groundLevel,
        visibility,
        windSpeed,
        windDeg,
        clouds,
        sunrise,
        sunset,
        timeZone,
        lastUpdate
    )
}


//---------------------------------------------------------------------------------------------
fun shouldUpdateData(cachedTimestamp: Long): Boolean {
    val currentTimeInSec = System.currentTimeMillis() / 1000
    return (currentTimeInSec - cachedTimestamp) > CACHE_EXPIRY_TIME_IN_SEC
}
//---------------------------------------------------------------------------------------------

@DrawableRes
fun String.toDrawableRes(): Int {
    return when (this) {
        "01d" -> {
            R.drawable.ic_01d
        }

        "02d" -> {
            R.drawable.ic_02d
        }

        "03d" -> {
            R.drawable.ic_03d
        }

        "04d" -> {
            R.drawable.ic_04d
        }

        "09d" -> {
            R.drawable.ic_09d
        }

        "10d" -> {
            R.drawable.ic_10d
        }

        "11d" -> {
            R.drawable.ic_11d
        }

        "13d" -> {
            R.drawable.ic_13d
        }

        "01n" -> {
            R.drawable.ic_01n
        }

        "02n" -> {
            R.drawable.ic_02n
        }

        "03n" -> {
            R.drawable.ic_03n
        }

        "04n" -> {
            R.drawable.ic_04n
        }

        "10n" -> {
            R.drawable.ic_10n
        }

        "11n" -> {
            R.drawable.ic_11d
        }

        "13n" -> {
            R.drawable.ic_13n
        }

        "50n" -> {
            R.drawable.ic_50n
        }

        "50d" -> {
            R.drawable.ic_50d
        }

        else -> {
            R.drawable.cloudy_day
        }
    }
}