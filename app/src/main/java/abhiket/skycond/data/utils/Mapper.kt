package abhiket.skycond.data.utils

import abhiket.skycond.domain.model.City
import abhiket.skycond.domain.model.CityWeather
import abhiket.skycond.domain.model.Weather

const val EMPTY_STRING = ""


@JvmName("toCity")
internal fun abhiket.skycond.data.remote.model.City.asCity(): City {
    return City(
        id = 50,// TODO Update with real id
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        state = this.state ?: EMPTY_STRING,
        country = this.country ?: EMPTY_STRING
    )
}

internal fun abhiket.City.asCity(): City {
    return City(
        id = this.id, // TODO Update with real id
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        state = this.state ?: EMPTY_STRING,
        country = this.country ?: EMPTY_STRING
    )
}

internal fun abhiket.Weather.asCityWeather(city: City): CityWeather {
    return CityWeather(
        city = city,
        weather = Weather(
            conditionId = conditionId,
            main = main,
            description = description,
            icon = icon,
            temp = temp,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            seaLevel = seaLevel ?: 0L,
            groundLevel = groundLevel ?: 0L,
            visibility = visibility,
            windSpeed = windSpeed,
            windDeg = windDeg,
            clouds = clouds,
            sunrise = sunrise,
            sunset = sunset,
            timeZone = timeZone,
            lastUpdate = lastUpdate,
        )
    )
}

internal fun abhiket.Weather.asWeather(): Weather {
    return Weather(
        conditionId = conditionId,
        main = main,
        description = description,
        icon = icon,
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel ?: 0L,
        groundLevel = groundLevel ?: 0L,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        clouds = clouds,
        sunrise = sunrise,
        sunset = sunset,
        timeZone = timeZone,
        lastUpdate = lastUpdate,
    )
}

internal fun abhiket.CityWeather.asCityWeather(): CityWeather {
    return CityWeather(
        city = City(
            id = cityId,
            name = cityName,
            latitude = cityLatitude,
            longitude = cityLongitude,
            state = cityState,
            country = cityCountry
        ),
        weather = Weather(
            conditionId = conditionId,
            main = main,
            description = description,
            icon = icon,
            temp = temp,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            seaLevel = seaLevel ?: 0L,
            groundLevel = groundLevel ?: 0L,
            visibility = visibility,
            windSpeed = windSpeed,
            windDeg = windDeg,
            clouds = clouds,
            sunrise = sunrise,
            sunset = sunset,
            timeZone = timeZone,
            lastUpdate = lastUpdate,
        )
    )
}

internal fun abhiket.GetCityWeatherById.asCityWeather(): CityWeather {
    return CityWeather(
        city = City(
            id = cityId,
            name = cityName,
            latitude = cityLatitude,
            longitude = cityLongitude,
            state = cityState,
            country = cityCountry
        ),
        weather = Weather(
            conditionId = conditionId,
            main = main,
            description = description,
            icon = icon,
            temp = temp,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            seaLevel = seaLevel ?: 0L,
            groundLevel = groundLevel ?: 0L,
            visibility = visibility,
            windSpeed = windSpeed,
            windDeg = windDeg,
            clouds = clouds,
            sunrise = sunrise,
            sunset = sunset,
            timeZone = timeZone,
            lastUpdate = lastUpdate,
        )
    )
}