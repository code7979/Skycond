package abhiket.skycond.uitls


import abhiket.WeatherDataEntity
import abhiket.skycond.R
import abhiket.skycond.data.remote.model.WeatherDataDto
import abhiket.skycond.presentation.model.Location
import abhiket.skycond.presentation.model.WeatherDataUi
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import abhiket.skycond.data.remote.model.City

const val CACHE_EXPIRY_TIME_IN_SEC = 600 //Second ( 10 minutes )
const val TAG = "WeatherApp_Skycond"

fun shouldUpdateData(cachedTimestamp: Long): Boolean {
    val currentTimeInSec = System.currentTimeMillis() / 1000
    return (currentTimeInSec - cachedTimestamp) > CACHE_EXPIRY_TIME_IN_SEC
}

fun WeatherDataDto.toWeatherDataUi(): WeatherDataUi {
    return WeatherDataUi(
        id = this.id,
        name = this.name,
        lat = this.coordinates.lat,
        lon = this.coordinates.lon,
        description = this.weather.first().description,
        temp = this.main.temp.toCelsius(),
        feelsLike = this.main.feelsLike.toCelsius(),
        tempMin = this.main.tempMin.toCelsius(),
        tempMax = this.main.tempMax.toCelsius(),
        humidity = "${this.main.humidity} %",
        clouds = "${this.clouds.all} %",
        pressure = this.main.pressure.tokPa(),
        visibility = "${this.visibility / 1000} Km",
        winSpeed = this.wind.speed.toKmh(),
        windDeg = "${this.wind.deg} Deg",
        icon = this.weather.first().icon.toDrawableRes(),
        date = convertLongToDate(this.date * 1000)
    )
}

fun WeatherDataEntity.toWeatherDataUi(): WeatherDataUi {
    return WeatherDataUi(
        id = this.cityId,
        name = this.cityName,
        lat = this.cityLatitude,
        lon = this.cityLongitude,
        description = this.description,
        temp = this.temp.toCelsius(),
        feelsLike = this.feelsLike.toCelsius(),
        tempMin = this.tempMin.toCelsius(),
        tempMax = this.tempMax.toCelsius(),
        humidity = "${this.humidity} %",
        clouds = "${this.clouds} %",
        pressure = this.pressure.tokPa(),
        visibility = "${this.visibility / 1000} Km",
        winSpeed = this.windSpeed.toKmh(),
        windDeg = "${this.windDeg} Deg",
        icon = this.icon.toDrawableRes(),
        date = convertLongToDate(this.lastUpdate * 1000)
    )
}

fun WeatherDataDto.toWeatherDataEntity(): WeatherDataEntity {
    return WeatherDataEntity(
        cityId = this.id,
        cityName = this.name,
        cityCountry = this.sys.country,
        cityLatitude = this.coordinates.lat,
        cityLongitude = this.coordinates.lon,
        lastUpdate = this.date,
        timeZone = this.timezone,
        description = this.weather.first().main,
        icon = this.weather.first().icon,
        temp = this.main.temp,
        feelsLike = this.main.feelsLike,
        tempMin = this.main.tempMin,
        tempMax = this.main.tempMax,
        pressure = this.main.pressure,
        humidity = this.main.humidity,
        seaLevel = this.main.seaLevel,
        groundLevel = this.main.groundLevel,
        visibility = this.visibility,
        windSpeed = this.wind.speed,
        windDeg = this.wind.deg,
        clouds = this.clouds.all,
        sunrise = this.sys.sunrise,
        sunset = this.sys.sunset

    )
}

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


@StringRes
fun Int.toStringRes(): Int {
    return when (this) {
        404 -> R.string.http_error_404
        403 -> R.string.http_error_403
        500 -> R.string.http_error_500
        502 -> R.string.http_error_502
        503 -> R.string.http_error_503
        else -> R.string.http_fetch_data_error
    }
}

fun City.toLocation(isAdded: Boolean): Location {
    return Location(
        name = this.name ?: "No Name",
        state = this.state ?: "No Name",
        country = this.country ?: "No Name",
        latitude = this.lat ?: 0.0,
        longitude = this.lon ?: 0.0,
        isAdded = isAdded ?: false
    )
}