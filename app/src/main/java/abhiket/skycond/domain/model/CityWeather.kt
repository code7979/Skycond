package abhiket.skycond.domain.model

data class CityWeather(
    val city: City,
    val weather: Weather
) {
    override fun toString(): String = buildString {
        append("WeatherData(\n")
        append("  id=${city.id},\n")
        append("  name=${city.name},\n")
        append("  country=${city.country},\n")
        append("  latitude=${city.latitude},\n")
        append("  longitude=${city.longitude},\n")
        append("  lastUpdate=${weather.lastUpdate},\n")
        append("  main=${weather.main},\n")
        append("  temp=${weather.temp},\n")
        append("  feelsLike=${weather.feelsLike},\n")
        append("  tempMin=${weather.tempMin},\n")
        append("  tempMax=${weather.tempMax},\n")
        append("  pressure=${weather.pressure},\n")
        append("  humidity=${weather.humidity},\n")
        append("  seaLevel=${weather.seaLevel},\n")
        append("  groundLevel=${weather.groundLevel},\n")
        append("  visibility=${weather.visibility},\n")
        append("  winSpeed=${weather.windSpeed},\n")
        append("  windDeg=${weather.windDeg},\n")
        append("  clouds=${weather.clouds},\n")
        append("  sunrise=${weather.sunrise},\n")
        append("  sunset=${weather.sunset},\n")
        append("  timeZone=${weather.timeZone}\n")
        append(")")
    }
}